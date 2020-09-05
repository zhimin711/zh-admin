<template>
  <div>
    <Card>
      <Form ref="formInline" :model="query1.params" inline :label-width="80">
        <FormItem prop="username" label="用户名">
          <Input type="text" v-model="query1.params.username" placeholder="用户名">
          </Input>
        </FormItem>
        <FormItem prop="name" label="用户名称">
          <Input type="text" v-model="query1.params.name" placeholder="名称">
          </Input>
        </FormItem>
      </Form>
      <Button style="margin: 5px 3px;" type="primary" icon="md-search" @click="handleSearch">查询</Button>
      <Button style="margin: 5px 3px;" type="primary" icon="md-add" @click="handleAdd">新增</Button>
      <Table ref="tables" :data="tableData" :columns="columns">
        <template slot-scope="{ row, index }" slot="action">
          <ButtonGroup size="small">
            <Button icon="ios-create-outline" @click="handleEdit(row, index)">编辑</Button>
            <Button icon="ios-remove" @click="handleDelete(row,index)">删除</Button>
          </ButtonGroup>
        </template>
      </Table>
      <Page :total="tablePager.total" show-sizer show-elevator show-total />

      <Modal v-model="recordModal" @on-cancel="cancelRecord">
        <p slot="header">
          用户
          <span v-if="recordModalType === 'add'">新增</span>
          <span v-if="recordModalType === 'edit'">修改</span>
        </p>
        <Form ref="recordForm" :model="record" :rules="recordRules" :label-width="80">
          <FormItem label="代码" prop="code" required>
            <i-input type="text" v-model="record.code" :disabled="codeDisabled"></i-input>
          </FormItem>
          <FormItem label="名称" prop="name" required>
            <i-input type="text" v-model="record.name"></i-input>
          </FormItem>
        </Form>
        <div slot="footer">
          <Button type="primary" @click="handleSubmit('recordForm')" :loading="loading">提交</Button>
          <Button @click="cancelRecord" style="margin-left: 8px" :disabled="loading">取消</Button>
        </div>
      </Modal>
<!--      <Modal v-model="roleModal" size="large">
        <p slot="header">
          请注意，您当前正在对<span style="color:red; vertical-align: top;">{{formCustom.userName}}</span> 用户进行角色分配操作！
        </p>
        <Form ref="formCustom2" :model="formCustom" :label-width="80">
          <FormItem label="所属角色" prop="roles">
            <Select v-model="formCustom.roleIds" multiple style="width:260px">
              <Option v-for="item in roleData" :value="item.value" :key="item.value">{{ item.name }}</Option>
            </Select>
          </FormItem>
        </Form>
        <div slot="footer">
          <Button type="primary" @click="handleSubmit('formCustom2')" :loading="loading">提交</Button>
          <Button @click="()=>{this.setRoleModal = false;this.formCustom.roles = ''}" style="margin-left: 8px"
                  :disabled="loading">取消
          </Button>
        </div>
      </Modal>-->
    </Card>
  </div>
</template>

<script>
import Tables from '_c/tables'
import { getPageRole } from '@/api/upms/role'

const defaultRecord = {
  code: '',
  name: ''
}
export default {
  name: 'UpmsRole',
  components: {
    Tables
  },
  data () {
    return {
      loading: false,
      recordModal: false,
      recordModalType: 'add',
      codeDisabled: false,
      record: {
        username: '',
        password: ''
      },
      recordRules: {},
      roleModal: false,
      columns: [
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
      query1: {
        page: 1,
        pageSize: 10,
        params: {}
      },
      tableData: [],
      tablePager: {
        total: 0
      }
    }
  },
  methods: {
    handleSearch () {
      this.getPage()
    },
    handleAdd () {
      this.recordModal = true
      this.recordModalType = 'add'
      this.codeDisabled = false
      this.record = Object.assign({}, defaultRecord)
    },
    handleEdit (row) {
      this.recordModal = true
      this.recordModalType = 'edit'
      this.codeDisabled = true
      this.record = Object.assign({}, row)
    },
    handleDelete (params) {
      console.log(params)
    },
    cancelRecord () {
      this.recordModal = false
      this.$refs.recordForm.resetFields()
    },
    handleSubmit () {
      console.log(this.record)
    },
    getPage () {
      getPageRole(this.query1).then(res => {
        this.tableData = res.data.rows
        this.tablePager.total = res.data.total
      })
    }
  },
  mounted () {
    this.getPage()
  }
}
</script>

<style>

</style>
