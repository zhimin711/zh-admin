<template>
  <div>
    <Card>
      <tables ref="tables" editable searchable search-place="top" v-model="tableData" :columns="columns" @on-delete="handleDelete"/>
      <Button style="margin: 10px 0;" type="primary" @click="exportExcel">查询</Button>
      <Page :total="tablePager.total" show-sizer show-elevator show-total />

      <Modal v-model="userModal">
        <p slot="header">
          请注意，您当前正在对<span style="color:red; vertical-align: top;">{{record.username}}</span> 用户进行密码重置操作！
        </p>
        <Form ref="formCustom" :model="record" :rules="recordRules" :label-width="80">
          <FormItem label="新密码" prop="password" required>
            <i-input type="password" v-model="record.password"></i-input>
          </FormItem>
          <FormItem label="确认密码" prop="passwdCheck">
            <i-input type="password" v-model="record.passwdCheck"></i-input>
          </FormItem>
        </Form>
        <div slot="footer">
          <Button type="primary" @click="handleSubmit('record')" :loading="loading">提交</Button>
          <Button @click="()=>{this.userModal = false}" style="margin-left: 8px" :disabled="loading">取消
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
export default {
  name: 'UpmsUser',
  components: {
    Tables
  },
  data () {
    return {
      loading: false,
      userModal: false,
      record: {
        username: '',
        password: ''
      },
      recordRules: {},
      roleModal: false,
      columns: [
        { title: '用户名', key: 'username', editable: true },
        { title: '用户昵称', key: 'name', sortable: true },
        { title: '创建时间', key: 'creationDate' },
        {
          title: '操作',
          key: 'handle',
          options: ['delete'],
          button: [
            (h, params, vm) => {
              return h('Poptip', {
                props: {
                  confirm: true,
                  title: '你确定要删除吗?'
                },
                on: {
                  'on-ok': () => {
                    vm.$emit('on-delete', params)
                    vm.$emit('input', params.tableData.filter((item, index) => index !== params.row.initRowIndex))
                  }
                }
              }, [
                h('Button', '自定义删除')
              ])
            }
          ]
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
    handleDelete (params) {
      console.log(params)
    },
    exportExcel () {
      this.exportEdit()
    },
    exportEdit () {
      this.userModal = true
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
