<template>
  <div class="app-container">
    <div class="filter-container">
      <el-input v-model="listQuery.name" placeholder="Instance 名称" style="width: 200px;" class="filter-item" />
      <el-select v-model="listQuery.clusterServerId" placeholder="所属集群/主机" class="filter-item">
        <el-option key="" label="所属集群/主机" value="" />
        <el-option-group v-for="group in options" :key="group.label" :label="group.label">
          <el-option v-for="item in group.options" :key="item.value" :label="item.label" :value="item.value" />
        </el-option-group>
      </el-select>
      <el-button class="filter-item" type="primary" icon="el-icon-search" plain @click="queryData()">查询</el-button>
      &nbsp;&nbsp;
      <el-button class="filter-item" type="primary" @click="handleCreate()">新建 Instance</el-button>
      <el-button class="filter-item" type="info" @click="fetchData()">刷新列表</el-button>
    </div>
    <el-table
      v-loading="listLoading"
      :data="list"
      element-loading-text="Loading"
      border
      fit
      highlight-current-row
    >
      <el-table-column label="Instance 名称" min-width="200" align="center">
        <template slot-scope="scope">
          {{ scope.row.name }}
        </template>
      </el-table-column>
      <el-table-column label="所属集群" min-width="200" align="center">
        <template slot-scope="scope">
          <span v-if="scope.row.canalCluster !== null">
            {{ scope.row.canalCluster.name }}
          </span>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column label="所属主机" min-width="200" align="center">
        <template slot-scope="scope">
          <span v-if="scope.row.nodeServer !== null">
            {{ scope.row.nodeServer.name }}
          </span>
          <span v-else>-</span>
        </template>
      </el-table-column>
      <el-table-column class-name="status-col" label="状态" min-width="150" align="center">
        <template slot-scope="scope">
          <el-tag :type="scope.row.runningStatus | statusFilter">{{ scope.row.runningStatus | statusLabel }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="修改时间" min-width="200" align="center">
        <template slot-scope="scope">
          {{ scope.row.modifiedTime }}
        </template>
      </el-table-column>
      <el-table-column align="center" prop="created_at" label="操作" min-width="150">
        <template slot-scope="scope">
          <el-dropdown trigger="click">
            <el-button type="primary" size="mini">
              操作<i class="el-icon-arrow-down el-icon--right" />
            </el-button>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item @click.native="handleUpdate(scope.row)">修改</el-dropdown-item>
              <el-dropdown-item @click.native="handleDelete(scope.row)">删除</el-dropdown-item>
              <el-dropdown-item @click.native="handleStart(scope.row)">启动</el-dropdown-item>
              <el-dropdown-item @click.native="handleStop(scope.row)">停止</el-dropdown-item>
              <el-dropdown-item @click.native="handleLog(scope.row)">日志</el-dropdown-item>
              <el-dropdown-item @click.native="handleCursor(scope.row)">Redis游标</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </template>
      </el-table-column>
    </el-table>
    <pagination v-show="count>0" :total="count" :page.sync="listQuery.page" :limit.sync="listQuery.size" @pagination="fetchData()" />

    <el-dialog :visible.sync="dialogFormVisible" title="Reids游标信息" width="600px">
      <el-form ref="dataForm" :model="metaPosition" label-position="left" label-width="100px" style="width: 400px; margin-left:30px;">
        <el-form-item label="游标地址">
          <el-input v-model="metaPosition.identity.sourceAddress" disabled />
        </el-form-item>
        <el-form-item label="游标Binlog">
          <el-input v-model="metaPosition.postion.journalName" />
          <span class="form-item-desc">
            <b style="font-size: 12px">show binary logs;</b><br>可查询当前实例Binlog
          </span>
        </el-form-item>
        <el-form-item label="当前游标位置">
          <el-input v-model="metaPosition.postion.position" />
          <span class="form-item-desc">
            <b style="font-size: 12px">show BINLOG EVENTS in 'mysql-bin.xxxx' from xx;</b><br>可查询Binlog可用位置
          </span>
        </el-form-item>
        <el-form-item label="游标时间戳">
          <el-input v-model="metaPosition.postion.timestamp" disabled />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogFormVisible = false">取消</el-button>
        <el-button type="primary" @click="handleCursorUpdate()">更新</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import { getCanalInstances, deleteCanalInstance, instanceStatus, getRedisPosition, updateRedisPosition } from '@/api/canalInstance'
import Pagination from '@/components/Pagination'
import { getClustersAndServers } from '@/api/canalCluster'

export default {
  components: { Pagination },
  filters: {
    statusFilter(status) {
      const statusMap = {
        '1': 'success',
        '0': 'gray'
      }
      return statusMap[status]
    },
    statusLabel(status) {
      const statusMap = {
        '1': '启动',
        '0': '停止'
      }
      return statusMap[status]
    }
  },
  data() {
    return {
      list: null,
      listLoading: true,
      dialogFormVisible: false,
      nodeServices: [],
      count: 0,
      options: [],
      listQuery: {
        name: '',
        clusterServerId: '',
        page: 1,
        size: 20
      },
      metaPosition: { identity: {}, postion: {}},
      currentId: null,
      rules: {
        id: [{ required: true, message: '请选择运行Server', trigger: 'change' }]
      }
    }
  },
  created() {
    getClustersAndServers().then((res) => {
      this.options = res.data
    })
    this.fetchData()
  },
  methods: {
    queryData() {
      this.listQuery.page = 1
      this.fetchData()
    },
    fetchData() {
      this.listLoading = true
      getCanalInstances(this.listQuery).then(res => {
        this.list = res.data.items
        this.count = res.data.count
      }).finally(() => {
        this.listLoading = false
      })
    },
    handleCreate() {
      this.$router.push('/canalServer/canalInstance/add')
    },
    handleUpdate(row) {
      this.$router.push('/canalServer/canalInstance/modify?id=' + row.id)
    },
    handleDelete(row) {
      this.$confirm('删除Instance配置会导致停止', '确定删除Instance信息', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        deleteCanalInstance(row.id).then((res) => {
          if (res.data === 'success') {
            this.fetchData()
            this.$message({
              message: '删除Instance信息成功',
              type: 'success'
            })
          } else {
            this.$message({
              message: '删除Instance信息失败',
              type: 'error'
            })
          }
        })
      })
    },
    handleStart(row) {
      // if (row.runningStatus === '1') {
      //   this.$message({ message: '当前Instance已处于启动状态！', type: 'error' })
      //   return
      // }
      this.$confirm('启动Instance: ' + row.name, '确定启动Instance服务', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        instanceStatus(row.id, 'start').then((res) => {
          if (res.data) {
            this.fetchData()
            this.$message({
              message: '启动成功, 稍后请刷新列表查看状态',
              type: 'success'
            })
          } else {
            this.$message({
              message: '启动Instance出现异常',
              type: 'error'
            })
          }
        })
      })
    },
    handleStop(row) {
      // if (row.runningStatus === '0') {
      //   this.$message({ message: '当前Instance已处于停止状态！', type: 'error' })
      //   return
      // }
      this.$confirm('停止Instance: ' + row.name, '确定停止Instance服务', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        instanceStatus(row.id, 'stop').then((res) => {
          if (res.data) {
            this.fetchData()
            this.$message({
              message: '停止成功, 稍后请刷新列表查看状态',
              type: 'success'
            })
          } else {
            this.$message({
              message: '停止Instance出现异常',
              type: 'error'
            })
          }
        })
      })
    },
    handleLog(row) {
      if (row.nodeServer === null) {
        this.$message({ message: '当前Instance不是启动状态，无法查看日志', type: 'warning' })
        return
      }
      this.$router.push('canalInstance/log?id=' + row.id + '&nodeId=' + row.nodeServer.id)
    },
    handleCursor(row) {
      this.listLoading = true
      getRedisPosition(row.id).then(res => {
        if (res.data) {
          this.metaPosition = res.data
          this.metaPosition.instanceId = row.id
          this.metaPosition.isStop = row.nodeServer === null
          this.dialogFormVisible = true
        } else {
          this.$message({ message: '当前Instance未使用Redis游标或未开始同步数据', type: 'warning' })
        }
      }).finally(() => {
        this.listLoading = false
      })
    },
    handleCursorUpdate() {
      if (!this.metaPosition.isStop) {
        this.$message({ message: '当前Instance不是停止状态，无法修改游标', type: 'warning' })
        return
      }
      this.$confirm('确定游标吗？（注：只能更新停止的实例游标）', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        updateRedisPosition(this.metaPosition).then((res) => {
          if (res.data) {
            this.dialogFormVisible = false
            this.$message({
              message: '更新成功，启动实例将以新的游标开始同步数据',
              type: 'success'
            })
          } else {
            this.$message({
              message: '更新失败！',
              type: 'error'
            })
          }
        })
      })
    }
  }
}
</script>
