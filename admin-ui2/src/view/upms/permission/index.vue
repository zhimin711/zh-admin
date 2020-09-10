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
      <TreeTable
        ref="tables"
        expand-key="code"
        :expand-type="false"
        :selectable="false"
        :tree-type="true"
        :data="tableData"
        :columns="columns"
        :loading="loading"
        @row-click="handleClick"
        @row-dblclick="handleClick"
      >
        <template slot="actions" slot-scope="{ row, index }">
          <ButtonGroup size="small">
            <Button icon="ios-create-outline" @click="handleEdit(row, index)">编辑</Button>
            <Button icon="ios-copy-outline" @click="handleEdit(row, index, 'copy')">复制</Button>
            <Button icon="ios-close" @click="handleDelete(row,index)">删除</Button>
          </ButtonGroup>
        </template>
      </TreeTable>
      <Page :total="pq.total" :current.sync="pq.page" @on-change="handlePage" @on-page-size-change="handleSize" show-sizer show-elevator show-total />

      <Modal v-model="recordModal" @on-cancel="cancelRecord" class-name="vertical-center-modal">
        <p slot="header">
          <span v-if="recordModalType === 'add'">{{ $t('addText') }}</span>
          <span v-if="recordModalType === 'edit'">{{ $t('editText') }}</span>
          <span>&nbsp;权限</span>
        </p>
        <Form ref="recordForm" :model="record" :rules="recordRules" :label-width="80">
          <FormItem label="类型" prop="type">
            <RadioGroup v-model="record.type" type="button" @on-change="getParents">
              <Radio label="C">目录</Radio>
              <Radio label="M">菜单</Radio>
              <Radio label="B">按钮</Radio>
            </RadioGroup>
          </FormItem>
          <FormItem label="上级" prop="parentId">
            <Cascader :data="options.parent" v-model="values.parent" :render-format="parentFormat"></Cascader>
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
          <FormItem v-if="record.type !== 'B'" label="图标" prop="icon">
            <i-input type="text" v-model="record.icon"></i-input>
          </FormItem>
          <FormItem label="序号" prop="sort">
            <InputNumber :max="1000" :min="1" v-model="record.sort"></InputNumber>
          </FormItem>
          <FormItem v-if="record.type === 'M'" label="隐藏" prop="status">
            <i-switch size="large" v-model="record.hidden">
              <span slot="open">是</span>
              <span slot="close">否</span>
            </i-switch>
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
    </Card>
  </div>
</template>

<script>
import { getPagePermission, addPermission, editPermission, getParentPermission } from '@/api/upms/permission'

const defaultRecord = {
  type: 'C',
  code: '',
  name: '',
  sort: 1,
  status: '1'
}
export default {
  name: 'upmsPermission',
  data () {
    return {
      loading: false,
      columns: [
        { title: '代码', key: 'code' },
        { title: '名称', key: 'name' },
        // { title: '创建时间', key: 'createAt' },
        { title: '路由或地址', key: 'url' },
        {
          title: '操作',
          key: 'actions',
          width: 200,
          align: 'center',
          type: 'template',
          template: 'actions'
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
      recordStatus: false,
      options: {
        parent: []
      },
      values: {
        parent: [],
        label: []
      }
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
      this.values.parent = []
      this.getParents()
    },
    handleEdit (row, index, op) {
      this.recordModal = true
      this.recordModalType = 'edit'
      this.disabledProps.code = true
      this.record = Object.assign({}, row)

      if (op && op === 'copy') {
        this.recordModalType = 'add'
        this.disabledProps.code = false
        this.record.id = undefined
        this.record.sort += 1
      }

      this.recordStatus = this.record.status === '1'
      this.getParents(row.type)
      this.values.parent = []
      if (row.parentId !== '0') {
        this.values.parent = row.parentId.split(',')
      }
    },
    handleDelete (params) {
      console.log(params)
    },
    handleClick (params) {
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

      if (this.values.parent.length > 0) {
        this.record.parentId = this.values.parent.join(',')
        this.record.parentName = this.values.label.join(',')
      } else {
        this.record.parentId = undefined
        this.record.parentName = undefined
      }

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
    getParents (type) {
      let t = 'c'
      if (type === 'B') {
        t = 'm'
      }
      this.loading = true
      getParentPermission(t).then(res => {
        if (res.data.success) {
          this.options.parent = res.data.rows
        }
      }).finally(() => { this.loading = false })
    },
    parentFormat (label) {
      this.values.label = label
      return label.join('/')
    },
    getPage () {
      this.loading = true
      getPagePermission(this.pq).then(res => {
        const { data } = res
        this.tableData = data.rows
        this.pq.total = data.total
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
