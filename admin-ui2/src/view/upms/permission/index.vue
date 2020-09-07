<template>
  <div>
    <Card>
      <Form ref="formInline" :model="pq.params" inline :label-width="80">
        <FormItem prop="code" label="代码">
          <Input type="text" v-model="pq.params.code" placeholder="代码">
          </Input>
        </FormItem>
        <FormItem prop="name" label="名称">
          <Input type="text" v-model="pq.params.name" placeholder="名称">
          </Input>
        </FormItem>
      </Form>
      <Button style="margin: 5px 3px;" type="primary" icon="md-search" @click="handleSearch" :loading="loading">{{ $t('searchText') }}</Button>
      <Button style="margin: 5px 3px;" type="primary" icon="md-add" @click="handleAdd">{{ $t('addText') }}</Button>
      <tree-table ref="tables" expand-key="code" :expand-type="false" :selectable="false" :data="tableData" :columns="columns" :loading="loading">
        <template slot-scope="{ row, index }" slot="action">
          <ButtonGroup size="small">
            <Button icon="ios-create-outline" @click="handleEdit(row, index)">编辑</Button>
            <Button icon="ios-close" @click="handleDelete(row,index)">删除</Button>
          </ButtonGroup>
        </template>
      </tree-table>
      <Page :total="pq.total" :current.sync="pq.page" @on-change="handlePage" @on-page-size-change="handleSize" show-sizer show-elevator show-total />

      <Modal v-model="recordModal" @on-cancel="cancelRecord" class-name="vertical-center-modal">
        <p slot="header">
          <span v-if="recordModalType === 'add'">{{ $t('addText') }}</span>
          <span v-if="recordModalType === 'edit'">{{ $t('editText') }}</span>
          <span>&nbsp;权限</span>
        </p>
        <Form ref="recordForm" :model="record" :rules="recordRules" :label-width="80">
          <FormItem label="类型" prop="type">
            <RadioGroup v-model="record.type" type="button">
              <Radio label="C">目录</Radio>
              <Radio label="M">菜单</Radio>
              <Radio label="B">按钮</Radio>
            </RadioGroup>
          </FormItem>
          <FormItem label="代码" prop="code" required>
            <i-input type="text" v-model="record.code" :disabled="disabledProps.code"></i-input>
          </FormItem>
          <FormItem label="名称" prop="name" required>
            <i-input type="text" v-model="record.name"></i-input>
          </FormItem>
          <FormItem label="地址" prop="url">
            <i-input type="text" v-model="record.url"></i-input>
          </FormItem>
          <FormItem label="图标" prop="icon">
            <i-input type="text" v-model="record.icon"></i-input>
          </FormItem>
          <FormItem label="序号" prop="sort">
            <InputNumber :max="1000" :min="1" v-model="record.sort"></InputNumber>
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
import { getPagePermission, addPermission, editPermission } from '@/api/upms/permission'

const defaultRecord = {
  type: 'C',
  code: '',
  name: '',
  sort: 1,
  status: '1'
}
export default {
  name: 'UpmsPermission',
  components: {
    Tables
  },
  data () {
    return {
      loading: false,
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
      pq: {
        params: {},
        page: 1,
        size: 10,
        total: 0
      },
      tableData: [],
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
    async handleSubmit () {
      let resp
      let op = ''
      this.record.status = this.recordStatus ? '1' : '0'

      this.loading = true
      if (this.recordModalType === 'add') {
        op = '新增'
        resp = await addPermission(this.record).finally(() => { this.loading = false })
      } else if (this.recordModalType === 'edit') {
        op = '修改'
        resp = await editPermission(this.record).finally(() => { this.loading = false })
      }
      if (resp && resp.data && resp.data.success) {
        this.$Message.success(`${op}权限 [${this.record.name}] 成功！`)
        this.cancelRecord()
        this.handleSearch()
      } else {
        this.$Message.error(`${op}权限 [${this.record.name}] 失败！`)
      }
    },
    getPage () {
      this.loading = true
      getPagePermission(this.pq).then(res => {
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
