<template>
  <div>
    <Card>
      <Form ref="formInline" :model="pq.params" inline :label-width="80">
        <FormItem prop="username" label="用户名">
          <Input type="text" v-model="pq.params.username" placeholder="用户名">
          </Input>
        </FormItem>
        <FormItem prop="name" label="用户名称">
          <Input type="text" v-model="pq.params.name" placeholder="名称">
          </Input>
        </FormItem>
      </Form>
      <Button style="margin: 5px 3px;" type="primary" icon="md-search" @click="handleSearch">{{ $t('searchText') }}</Button>
      <Button style="margin: 5px 3px;" icon="md-refresh" @click="pq.params = {}">{{ $t('resetText') }}</Button>
      <Button style="margin: 5px 3px;" type="primary" icon="md-add" @click="handleAdd">{{ $t('addText') }}</Button>
      <Button style="margin: 5px 3px;" type="success" icon="md-people" @click="handleRoles" :disabled="selectRows.length!==1">分配角色</Button>
      <Table ref="tables" :data="tableData" :columns="columns" @on-select="handleSelect" @on-select-cancel="handleSelect">
        <template slot-scope="{ row, index }" slot="action">
          <ButtonGroup size="small">
            <Button icon="ios-create-outline" @click="handleEdit(row, index)">编辑</Button>
            <Button icon="ios-remove" @click="handleDelete(row,index)">删除</Button>
          </ButtonGroup>
        </template>
      </Table>
      <Page :total="pq.total" :current.sync="pq.page" @on-change="handlePage" @on-page-size-change="handleSize" show-sizer show-elevator show-total />

      <Modal v-model="recordModal" @on-cancel="cancelRecord">
        <p slot="header">
          <span>用户</span>
          <span v-if="recordModalType === 'add'">新增</span>
          <span v-if="recordModalType === 'edit'">修改</span>
        </p>
        <Form ref="recordForm" :model="record" :rules="recordRules" :label-width="80">
          <FormItem label="账号" prop="userId" required>
            <i-input type="text" v-model="record.userId" :disabled="disabledProps.code"></i-input>
          </FormItem>
          <FormItem label="用户名" prop="username" required>
            <i-input type="text" v-model="record.username" :disabled="disabledProps.code"></i-input>
          </FormItem>
          <FormItem label="真实姓名" prop="realName" required>
            <i-input type="text" v-model="record.realName"></i-input>
          </FormItem>
          <FormItem label="性别">
            <RadioGroup v-model="recordSex">
              <Radio label="1">男</Radio>
              <Radio label="0">女</Radio>
            </RadioGroup>
          </FormItem>
          <FormItem label="出生日期" prop="birth">
            <DatePicker type="date" v-model="record.birth" placeholder="Select date" style="width: 200px" @on-change="changeDateBirth"></DatePicker>
          </FormItem>
          <FormItem label="手机号码" prop="mobile">
            <i-input type="text" v-model="record.mobile"></i-input>
          </FormItem>
          <FormItem label="电子邮箱" prop="email">
            <i-input type="text" v-model="record.email"></i-input>
          </FormItem>
          <FormItem label="过期日期" prop="expired">
            <DatePicker type="date" v-model="record.expired" placeholder="Select date" style="width: 200px"></DatePicker>
          </FormItem>
          <FormItem label="状态" prop="status">
            <i-switch size="large" v-model="recordStatus">
              <span slot="open">开启</span>
              <span slot="close">关闭</span>
            </i-switch>
          </FormItem>
        </Form>
        <div slot="footer">
          <Button type="primary" @click="handleSubmit('recordForm')" :loading="loading">提交</Button>
          <Button @click="cancelRecord" style="margin-left: 8px" :disabled="loading">取消</Button>
        </div>
      </Modal>
      <Modal v-model="roleModal" size="large">
        <p slot="header">
          请注意，您当前正在对 <span style="color:red; vertical-align: top;">{{record.username}}</span> 用户进行角色分配操作！
        </p>
        <!--:render-format="render3"-->
        <Transfer
          :data="roles"
          :target-keys="valueRoles"
          :list-style="listStyle"
          :titles="['可分配角色','已分配角色']"
          :operations="['移除','分配']"
          filterable
          @on-change="changeRole">
          <div :style="{float: 'right', margin: '5px'}">
            <Button size="small" @click="reloadRoles">{{ $t('refreshText') }}</Button>
          </div>
        </Transfer>
        <div slot="footer">
          <Button type="primary" @click="saveUserRoles" :loading="loading">提交</Button>
          <Button @click="()=>{this.roleModal = false;this.valueRoles = []}" style="margin-left: 8px"
                  :disabled="loading">取消
          </Button>
        </div>
      </Modal>
    </Card>
  </div>
</template>

<script>
import Tables from '_c/tables'
import { getPageUser, getUserRoles, saveUserRoles, addUser, editUser } from '@/api/upms/user'
import { getAllRole } from '@/api/upms/role'

const defaultR = {
  username: '',
  password: ''
}
export default {
  name: 'upmsUser',
  components: {
    Tables
  },
  data () {
    return {
      roleModal: false,
      roles: [],
      valueRoles: [],
      listStyle: {
        width: '200px',
        height: '300px'
      },
      loading: false,
      recordModal: false,
      recordModalType: 'add',
      disabledProps: {
        code: false,
        username: false
      },
      record: {},
      recordSex: '',
      recordStatus: true,
      recordRules: {},
      columns: [
        {
          type: 'selection',
          width: 60,
          align: 'center'
        },
        { title: '用户账号', key: 'userId' },
        { title: '用户名', key: 'username' },
        { title: '用户昵称', key: 'realName' },
        { title: '邮箱', key: 'email' },
        { title: '创建时间', key: 'createAt' },
        {
          title: '操作',
          slot: 'action',
          fixed: 'right',
          width: 160,
          align: 'center'
        }
      ],
      pq: {
        params: {},
        page: 1,
        size: 10,
        total: 0
      },
      selectRows: [],
      tableData: []
    }
  },
  methods: {
    handleSearch () {
      this.pq.page = 1
      this.getPageUser()
    },
    handlePage (val) {
      this.pq.page = val
      this.getPage()
    },
    handleSize (val) {
      this.pq.size = val
      this.getPage()
    },
    handleSelect (selection, row) {
      this.selectRows = selection
      if (selection.length === 1) {
        this.record = Object.assign({}, selection[0])
      }
    },
    handleAdd () {
      this.recordModal = true
      this.recordModalType = 'add'
      this.disabledProps.code = false
      this.record = Object.assign({}, defaultR)
    },
    handleEdit (row) {
      this.recordModal = true
      this.recordModalType = 'edit'
      this.disabledProps.code = true
      this.record = Object.assign({}, row)
      this.recordSex = undefined
      if (typeof row.sex === 'undefined' || row.sex === null) {
        this.recordSex = undefined
      } else if (row.sex) {
        this.recordSex = '1'
      } else {
        this.recordSex = '0'
      }
    },
    async handleSubmit () {
      let resp
      let op = ''
      this.record.status = this.recordStatus ? '1' : '0'

      if (typeof this.recordSex === 'undefined' || this.recordSex === null) {
        this.record.sex = undefined
      } else {
        this.record.sex = this.recordSex === '1'
      }

      this.loading = true
      if (this.recordModalType === 'add') {
        op = '新增'
        resp = await addUser(this.record).finally(() => { this.loading = false })
      } else if (this.recordModalType === 'edit') {
        op = '修改'
        resp = await editUser(this.record).finally(() => { this.loading = false })
      }
      if (resp && resp.data && resp.data.success) {
        this.$Message.success(`${op}用户 [${this.record.username}] 成功！`)
        this.cancelRecord()
        this.handleSearch()
      } else {
        this.$Message.error(`${op}用户 [${this.record.username}] 失败！`)
      }
    },
    handleDelete (params) {
      console.log(params)
    },
    cancelRecord () {
      this.recordModal = false
      this.$refs.recordForm.resetFields()
    },
    handleRoles () {
      if (this.selectRows.length !== 1) {
        return
      }
      this.roleModal = true
      this.record = Object.assign({}, this.selectRows[0])
      this.getUserRoles()
    },
    getRoles () {
      this.roles = []
      getAllRole().then(resp => {
        const { data } = resp
        if (data && data.success) {
          this.roles = data.rows
        }
      })
    },
    getUserRoles () {
      this.loading = true
      this.valueRoles = []
      getUserRoles(this.record.id).then(resp => {
        const { data } = resp
        if (data && data.success) {
          this.valueRoles = data.rows.map(item => item.id)
        }
      }).finally(() => {
        this.loading = false
      })
    },
    saveUserRoles () {
      saveUserRoles(this.record.id, this.valueRoles).then(resp => {
        const { data } = resp
        if (data && data.success) {
          this.roleModal = false
          this.$Message.success(` [${this.record.username}] 分配角色成功！`)
        } else {
          this.$Message.error(` [${this.record.username}] 分配角色失败！`)
        }
      })
    },
    changeRole (targetKeys) {
      this.valueRoles = targetKeys
    },
    changeDateBirth (date) {
      this.record.birth = date
    },
    reloadRoles () {
      this.getRoles()
      this.getUserRoles()
    },
    getPageUser () {
      getPageUser(this.pq).then(res => {
        this.tableData = res.data.rows
        this.pq.total = res.data.total
      })
    }
  },
  mounted () {
    this.getPageUser()
    this.getRoles()
  }
}
</script>

<style>
  .ivu-transfer-list-with-footer{
    padding-bottom: 0;
  }
</style>
