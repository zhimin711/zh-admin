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
          <Button type="primary" size="small" style="margin-right: 5px" @click="handleEdit(row, index)">View</Button>
          <Button type="error" size="small" @click="handleDelete(row,index)">Delete</Button>
        </template>
      </Table>
      <Page :total="tablePager.total" show-sizer show-elevator show-total />

      <Modal v-model="userModal">
        <p slot="header">
          用户 新增<!--{{record.username}}-->
        </p>
        <Form ref="recordForm" :model="record" :rules="recordRules" :label-width="80">
          <FormItem label="用户名" prop="username" required>
            <i-input type="text" v-model="record.username" :disabled="usernameDisabled"></i-input>
          </FormItem><FormItem label="新密码" prop="password" required>
            <i-input type="password" v-model="record.password"></i-input>
          </FormItem>
          <FormItem label="确认密码" prop="passwdCheck">
            <i-input type="password" v-model="record.passwdCheck"></i-input>
          </FormItem>
        </Form>
        <div slot="footer">
          <Button type="primary" @click="handleSubmit('recordForm')" :loading="loading">提交</Button>
          <Button @click="cancelRecord" style="margin-left: 8px" :disabled="loading">取消
          </Button>
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
import { getPageUser } from '@/api/upms/user'

const defaultR = {
  username: '',
  password: ''
}
export default {
  name: 'UpmsUser',
  components: {
    Tables
  },
  data () {
    return {
      loading: false,
      userModal: false,
      usernameDisabled: false,
      record: {
        username: '',
        password: ''
      },
      recordRules: {},
      roleModal: false,
      columns: [
        { title: '用户名', key: 'username' },
        { title: '用户昵称', key: 'name' },
        { title: '创建时间', key: 'creationDate' },
        {
          title: '操作',
          slot: 'action',
          fixed: 'right',
          width: 150,
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
      this.getPageUser()
    },
    handleAdd () {
      this.userModal = true
      this.usernameDisabled = false
      this.record = Object.assign({}, defaultR)
    },
    handleEdit (row) {
      this.userModal = true
      this.usernameDisabled = true
      this.record = Object.assign({}, row)
    },
    handleDelete (params) {
      console.log(params)
    },
    cancelRecord () {
      this.userModal = false
      this.$refs.recordForm.resetFields()
    },
    getPageUser () {
      getPageUser(this.query1).then(res => {
        this.tableData = res.data.rows
        this.tablePager.total = res.data.total
      })
    }
  },
  mounted () {
    this.getPageUser()
  }
}
</script>

<style>

</style>
