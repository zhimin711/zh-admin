<template>
  <div>
    <Card>
      <Form ref="formInline" :model="pq.params" inline :label-width="80">
        <FormItem prop="code" :label="$t('codeText')">
          <Input type="text" v-model="pq.params.code" :placeholder="$t('codeText')">
          </Input>
        </FormItem>
        <FormItem prop="name" :label="$t('nameText')">
          <Input type="text" v-model="pq.params.name" :placeholder="$t('nameText')">
          </Input>
        </FormItem>
        <FormItem :label="$t('statusText')">
          <Select v-model="pq.params.status">
            <Option value="">{{ $t('allText') }}</Option>
            <Option value="1">{{ $t('enabledText') }}</Option>
            <Option value="0">{{ $t('disabledText') }}</Option>
          </Select>
        </FormItem>
      </Form>
      <Button style="margin: 5px 3px;" type="primary" icon="md-search" @click="handleSearch" :loading="loading">{{ $t('searchText') }}</Button>
      <Button style="margin: 5px 3px;" icon="md-refresh" @click="pq.params = {}">{{ $t('resetText') }}</Button>
      <Button v-permission="['upmsRoleAdd']" style="margin: 5px 3px;" type="primary" icon="md-add" @click="handleAdd">{{ $t('addText') }}</Button>
      <Button v-permission="['upmsRolePermissionsSave']" style="margin: 5px 3px;" type="success" icon="md-lock" @click="handlePermission" :disabled="selectRows.length!==1">分配权限</Button>
      <Table ref="tables" :data="tableData" :columns="columns" :loading="loading" @on-select="handleSelect" @on-select-cancel="handleSelect">
        <template slot-scope="{ row, index }" slot="action">
          <ButtonGroup v-if="row.id !== 1" size="small">
            <Button v-permission="['upmsRoleEdit']" icon="ios-create-outline" @click="handleEdit(row, index)">编辑</Button>
            <Button v-permission="['upmsRoleDel']" icon="ios-close" @click="handleDelete(row,index)">删除</Button>
          </ButtonGroup>
        </template>
      </Table>
      <Page :total="pq.total" :current.sync="pq.page" @on-change="handlePage" @on-page-size-change="handleSize" show-sizer show-elevator show-total />

      <Modal v-model="recordModal" @on-cancel="cancelRecord" class-name="vertical-center-modal">
        <p slot="header">
          <span v-if="recordModalType === 'add'">{{ $t('addText') }}</span>
          <span v-if="recordModalType === 'edit'">{{ $t('editText') }}</span>
          <span>&nbsp;角色</span>
        </p>
        <Form ref="recordForm" :model="record" :rules="recordRules" :label-width="80">
          <FormItem label="代码" prop="code" required>
            <i-input type="text" v-model="record.code" :disabled="disabledProps.code"></i-input>
          </FormItem>
          <FormItem label="名称" prop="name" required>
            <i-input type="text" v-model="record.name"></i-input>
          </FormItem>
          <FormItem label="状态" prop="status">
            <i-switch size="large" v-model="recordStatus">
              <span slot="open">开启</span>
              <span slot="close">关闭</span>
            </i-switch>
          </FormItem>
        </Form>
        <div slot="footer">
          <Button type="primary" @click="handleSubmit('recordForm')" :loading="loadingModal">提交</Button>
          <Button @click="cancelRecord" style="margin-left: 8px" :disabled="loadingModal">取消</Button>
        </div>
      </Modal>
      <Modal v-model="permissionModal" size="large">
        <p slot="header">
          请注意，您当前正在对 <span style="color:red; vertical-align: top;">{{record.name}}</span> 角色进行权限分配操作！
        </p>
        <!--<Tree :data="valuesPermissions" show-checkbox :render="renderPermissionTree"></Tree>-->
        <Tree2 ref="permissionTree" :setting="treeSettings" :nodes="valuesPermissions"></Tree2>
        <div slot="footer">
          <Button type="primary" @click="saveRolePermissions" :loading="loadingModal">提交</Button>
          <Button @click="()=>{this.permissionModal = false;this.valuesPermissions = []}" style="margin-left: 8px" :disabled="loadingModal">取消
          </Button>
        </div>
      </Modal>
    </Card>
  </div>
</template>

<script>
import Tables from '_c/tables'
import Tree2 from 'vue-giant-tree'

import { getPageRole, addRole, editRole, getRolePermissions, saveRolePermissions } from '@/api/upms/role'
import { getAllPermission } from '@/api/upms/permission'

const defaultRecord = {
  code: '',
  name: '',
  status: '1'
}
export default {
  name: 'upmsRole',
  components: {
    Tables, Tree2
  },
  data () {
    return {
      loading: false,
      loadingModal: false,
      columns: [
        {
          type: 'selection',
          width: 60,
          align: 'center'
        },
        { title: '代码', key: 'code' },
        { title: '名称', key: 'name' },
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
      tableData: [],
      selectRows: [],
      treeSettings: {
        check: { enable: true }
      },
      permissionModal: false,
      permissions: [],
      valuesPermissions: [],
      recordModal: false,
      recordModalType: 'add',
      disabledProps: {
        all: false,
        code: false
      },
      record: {},
      recordRules: {},
      recordStatus: false
    }
  },
  methods: {
    handleSearch () {
      this.pq.page = 1
      this.getPage()
    },
    handlePage (val) {
      this.pq.page = val
      this.getPage()
    },
    handleSize (val) {
      this.pq.size = val
      this.getPage()
    },
    handleSelect (selection) {
      this.selectRows = selection
    },
    handleAdd () {
      this.recordModal = true
      this.recordModalType = 'add'
      this.disabledProps.code = false
      this.record = Object.assign({}, defaultRecord)
      this.recordStatus = this.record.status === '1'
    },
    handleEdit (row) {
      this.recordModal = true
      this.recordModalType = 'edit'
      this.disabledProps.code = true
      this.record = Object.assign({}, row)
      this.recordStatus = this.record.status === '1'
    },
    handleDelete (params) {
      console.log(params)
    },
    cancelRecord () {
      this.recordModal = false
      this.$refs.recordForm.resetFields()
    },
    handlePermission () {
      if (this.selectRows.length !== 1) {
        return
      } else if (this.selectRows[0].id === 1) {
        this.$Message.warning('超级拥有所有权，无需分配！')
        return
      }
      this.permissionModal = true
      this.record = Object.assign({}, this.selectRows[0])
      this.getRolePermissions()
    },
    getPermissions () {
      this.permissions = []
      getAllPermission().then(resp => {
        const { data } = resp
        if (data && data.success) {
          this.permissions = data.rows
          // this.valuesPermissions = data.rows
        }
      })
    },
    getRolePermissions () {
      this.loadingModal = true
      this.valuesPermissions = []
      getRolePermissions(this.record.id).then(resp => {
        const { data } = resp
        if (data && data.success) {
          this.filterPermissions(this.permissions, data.rows)
          this.valuesPermissions = Array.from(this.permissions)
        }
      }).finally(() => {
        this.loadingModal = false
      })
    },
    saveRolePermissions () {
      this.loadingModal = true
      let checkedNodes = this.$refs.permissionTree.ztreeObj.getCheckedNodes(true)
      const checkedIds = checkedNodes.map(item => item.id)
      saveRolePermissions(this.record.id, checkedIds).then(resp => {
        const { data } = resp
        if (data && data.success) {
          this.permissionModal = false
          this.$Message.success(` [${this.record.name}] 分配权限成功！`)
        } else {
          this.$Message.error(` [${this.record.name}] 分配权限失败！`)
        }
      }).finally(() => {
        this.loadingModal = false
      })
    },
    filterPermissions (allRows, selectRows) {
      allRows.forEach(item => {
        item.open = undefined
        item.checked = undefined
        // item.selected = undefined
        let hasChildren = item.children && item.children.length > 0
        for (let i = 0; i < selectRows.length; i++) {
          if (item.id === selectRows[i].id) {
            item.checked = true
            if (hasChildren) {
              item.open = true
              // item.selected = true
            } else {
              // item.checked = true
            }
            selectRows.splice(i, 1)
            break
          }
        }
        if (hasChildren) {
          this.filterPermissions(item.children, selectRows)
        }
      })
    },
    async handleSubmit () {
      let resp
      let op = ''
      this.record.status = this.recordStatus ? '1' : '0'

      this.loadingModal = true
      if (this.recordModalType === 'add') {
        op = '新增'
        resp = await addRole(this.record).finally(() => { this.loadingModal = false })
      } else if (this.recordModalType === 'edit') {
        op = '修改'
        resp = await editRole(this.record).finally(() => { this.loadingModal = false })
      }
      if (resp && resp.data && resp.data.success) {
        this.$Message.success(`${op}角色 [${this.record.name}] 成功！`)
        this.cancelRecord()
        this.handleSearch()
      } else {
        this.$Message.error(`${op}角色 [${this.record.name}] 失败！`)
      }
    },
    getPage () {
      this.loading = true
      getPageRole(this.pq).then(res => {
        this.tableData = res.data.rows
        this.pq.total = res.data.total
      }).finally(() => { this.loading = false })
    }
  },
  mounted () {
    this.handleSearch()
    this.getPermissions()
  }
}
</script>

<style lang="less">

</style>
