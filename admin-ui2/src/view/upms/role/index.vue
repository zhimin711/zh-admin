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
      <Button style="margin: 5px 3px;" type="primary" icon="md-add" @click="handleAdd">{{ $t('addText') }}</Button>
      <Button style="margin: 5px 3px;" type="success" icon="md-lock" @click="handlePermission" :disabled="selectRows.length!==1">分配权限</Button>
      <Table ref="tables" :data="tableData" :columns="columns" :loading="loading" @on-select="handleSelect" @on-select-cancel="handleSelect">
        <template slot-scope="{ row, index }" slot="action">
          <ButtonGroup size="small">
            <Button icon="ios-create-outline" @click="handleEdit(row, index)">编辑</Button>
            <Button icon="ios-close" @click="handleDelete(row,index)">删除</Button>
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
          <Button type="primary" @click="handleSubmit('recordForm')" :loading="loading">提交</Button>
          <Button @click="cancelRecord" style="margin-left: 8px" :disabled="loading">取消</Button>
        </div>
      </Modal>
      <Modal v-model="permissionModal" size="large">
        <p slot="header">
          请注意，您当前正在对 <span style="color:red; vertical-align: top;">{{record.name}}</span> 角色进行权限分配操作！
        </p>
        <Tree :data="permissions" show-checkbox></Tree>
        <div slot="footer">
          <Button type="primary" @click="handleSubmit" :loading="loading">提交</Button>
          <Button @click="()=>{this.permissionModal = false;this.valuesPermissions = []}" style="margin-left: 8px"
                  :disabled="loading">取消
          </Button>
        </div>
      </Modal>
    </Card>
  </div>
</template>

<script>
import Tables from '_c/tables'
import { getPageRole, addRole, editRole } from '@/api/upms/role'

const defaultRecord = {
  code: '',
  name: '',
  status: '1'
}
export default {
  name: 'upmsRole1',
  components: {
    Tables
  },
  data () {
    return {
      loading: false,
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
      permissionModal: false,
      permissions: [],
      valuesPermissions: [],
      recordModal: false,
      recordModalType: 'add',
      disabledProps: {
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
      }
      this.permissionModal = true
      this.record = Object.assign({}, this.selectRows[0])
    },
    async handleSubmit () {
      let resp
      let op = ''
      this.record.status = this.recordStatus ? '1' : '0'

      this.loading = true
      if (this.recordModalType === 'add') {
        op = '新增'
        resp = await addRole(this.record).finally(() => { this.loading = false })
      } else if (this.recordModalType === 'edit') {
        op = '修改'
        resp = await editRole(this.record).finally(() => { this.loading = false })
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
  }
}
</script>

<style lang="less">

</style>
