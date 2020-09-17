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
          <Select v-model="pq.params.status">>
            <Option value="">{{ $t('allText') }}</Option>
            <Option value="1">{{ $t('enabledText') }}</Option>
            <Option value="0">{{ $t('disabledText') }}</Option>
          </Select>
        </FormItem>
      </Form>
      <Button style="margin: 5px 3px;" type="primary" icon="md-search" @click="handleSearch" :loading="loading">{{ $t('searchText') }}</Button>
      <Button style="margin: 5px 3px;" icon="md-refresh" @click="pq.params = {}">{{ $t('resetText') }}</Button>
      <Button v-permission="['upmsPositionAdd']" style="margin: 5px 3px;" type="primary" icon="md-add" @click="handleAdd">{{ $t('addText') }}</Button>
      <Table ref="tables" :data="tableData" :columns="columns" :loading="loading">
        <template slot-scope="{ row, index }" slot="action">
          <ButtonGroup size="small">
            <Button v-permission="['upmsPositionEdit']" icon="ios-create-outline" @click="handleEdit(row, index)">编辑</Button>
            <Button v-permission="['upmsPositionDel']" icon="ios-close" @click="handleDelete(row,index)">删除</Button>
          </ButtonGroup>
        </template>
      </Table>
      <Page :total="pq.total" :current.sync="pq.page" @on-change="handlePage" @on-page-size-change="handleSize" show-sizer show-elevator show-total />

      <Modal v-model="recordModal" @on-cancel="cancelRecord" class-name="vertical-center-modal">
        <p slot="header">
          <span v-if="recordModalType === 'add'">新增</span>
          <span v-if="recordModalType === 'edit'">修改</span>
          <span>&nbsp;职位</span>
        </p>
        <Form ref="recordForm" :model="record" :rules="recordRules" :label-width="80">
          <FormItem label="编码" prop="code" required>
            <i-input type="text" v-model="record.code" :disabled="disabledProps.code"></i-input>
          </FormItem>
          <FormItem label="名称" prop="name" required>
            <i-input type="text" v-model="record.name"></i-input>
          </FormItem>
          <FormItem label="序号" prop="sort">
            <InputNumber :max="1000" :min="1" v-model="record.sort"></InputNumber>
          </FormItem>
          <FormItem label="备注" prop="remark">
            <Input v-model="record.remark" type="textarea" :autosize="{minRows: 2,maxRows: 5}" placeholder="请输入备注（说明）..."></Input>
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
import Tables from '_c/tables'
import { getPagePosition, addPosition, editPosition } from '@/api/upms/position'

const defaultRecord = {
  code: '',
  name: '',
  sort: 1,
  remark: '',
  status: '1'
}
export default {
  name: 'upmsPosition2',
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
        resp = await addPosition(this.record).finally(() => { this.loading = false })
      } else if (this.recordModalType === 'edit') {
        op = '修改'
        resp = await editPosition(this.record).finally(() => { this.loading = false })
      }
      if (resp && resp.data && resp.data.success) {
        this.$Message.success(`${op}职位 [${this.record.name}] 成功！`)
        this.cancelRecord()
        this.handleSearch()
      } else {
        this.$Message.error(`${op}职位 [${this.record.name}] 失败！`)
      }
    },
    getPage () {
      this.loading = true
      getPagePosition(this.pq).then(res => {
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
