<template>
  <Card shadow style="height: 100%;width: 100%;overflow:hidden">
    <div class="department-outer">
      <div class="tip-box">
        <b style="margin-right: 20px;">组织结构</b>
        <Button style="margin: 5px 3px;" type="primary" icon="md-refresh" @click="getDepartmentData" :loading="loading">{{ $t('refreshText') }}</Button>
      </div>
      <div class="zoom-box">
        <zoom-controller v-model="zoom" :min="20" :max="200"></zoom-controller>
      </div>
      <div class="view-box">
        <org-view
          v-if="data"
          :data="data"
          :zoom-handled="zoomHandled"
          @on-menu-click="handleMenuClick"
        ></org-view>
      </div>
    </div>
    <Modal v-model="recordModal" @on-cancel="cancelRecord" class-name="vertical-center-modal">
      <p slot="header">
        <span v-if="recordModalType === 'add'">{{ $t('addText') }}</span>
        <span v-if="recordModalType === 'edit'">{{ $t('editText') }}</span>
        <span>&nbsp;部门</span>
      </p>
      <Form ref="recordForm" :model="record" :rules="recordRules" :label-width="80">
        <FormItem label="上级" prop="parentId">
          <Cascader :data="options.parent" v-model="values.parent" :render-format="parentFormat" :disabled="disabledProps.parent" change-on-select></Cascader>
          <!--<i-input type="text" v-model="record.parentName" readonly :disabled="disabledProps.parent"></i-input>-->
        </FormItem>
        <FormItem label="名称" prop="name" required>
          <i-input type="text" v-model="record.name"></i-input>
        </FormItem>
        <FormItem label="负责人" prop="leader" required>
          <i-input type="text" v-model="record.leader"></i-input>
        </FormItem>
        <FormItem label="负责人电话" prop="phone">
          <i-input type="text" v-model="record.phone"></i-input>
        </FormItem>
        <FormItem label="负责人邮箱" prop="email">
          <i-input type="text" v-model="record.email"></i-input>
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
  </Card>
</template>

<script>
import OrgView from './components/org-view.vue'
import ZoomController from './components/zoom-controller.vue'
import { getTreeDepartment, getDepartment, addDepartment, editDepartment } from '@/api/upms/department'
import './index.less'
// const menuDic = {
//   edit: '编辑部门',
//   detail: '查看部门',
//   new: '新增子部门',
//   delete: '删除部门'
// }
const defaultRecord = {
  name: '',
  leader: '',
  phone: '',
  email: '',
  sort: 1,
  status: '1'
}
export default {
  name: 'upmsDepartment2',
  components: {
    OrgView,
    ZoomController
  },
  data () {
    return {
      loading: false,
      data: null,
      zoom: 100,
      tableData: [],
      recordModal: false,
      recordModalType: 'add',
      disabledProps: {
        parent: false
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
  computed: {
    zoomHandled () {
      return this.zoom / 100
    }
  },
  methods: {
    setDepartmentData (data) {
      data.isRoot = true
      return data
    },
    handleMenuClick ({ data, key }) {
      this.disabledProps.parent = data.isRoot || false
      if (key === 'delete') {
        return
      }
      this.values.parent = []
      const sort = data.children ? (data.children.length + 1) : 1
      const _that = this
      getDepartment(data.id).then(resp => {
        const { data } = resp
        const row = data.rows[0]
        _that.values.parent = row.parentId === '0' ? [] : row.parentId.split(',').map(id => Number(id))
        if (key === 'new') {
          _that.handleAdd()
          _that.record.pid = row.id
          _that.record.sort = sort
          _that.values.parent.push((row.id))
        } else if (key === 'edit') {
          _that.handleEdit(row)
        }
      })
    },
    handleAdd () {
      this.recordModal = true
      this.recordModalType = 'add'
      this.record = Object.assign({}, defaultRecord)
      this.recordStatus = this.record.status === '1'
    },
    handleEdit (row) {
      this.recordModal = true
      this.recordModalType = 'edit'
      this.record = Object.assign({}, row)
      this.recordStatus = this.record.status === '1'
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
        resp = await addDepartment(this.record).finally(() => { this.loading = false })
      } else if (this.recordModalType === 'edit') {
        op = '修改'
        resp = await editDepartment(this.record).finally(() => { this.loading = false })
      }
      if (resp && resp.data && resp.data.success) {
        this.$Message.success(`${op}部门 [${this.record.name}] 成功！`)
        this.cancelRecord()
        this.getDepartmentData()
        this.getParents()
      } else {
        this.$Message.error(`${op}部门 [${this.record.name}] 失败！`)
      }
    },
    getDetail (id) {
      // getDepartment(id)
    },
    parentFormat (label) {
      this.values.label = label
      return label.join('/')
    },
    getParents () {
      getTreeDepartment(2).then(res => {
        const { data } = res
        this.options.parent = data.rows
      })
    },
    getDepartmentData () {
      getTreeDepartment(1).then(res => {
        const { data } = res
        this.data = data.rows[0]
      })
    }
  },
  mounted () {
    this.getDepartmentData()
    this.getParents()
  }
}
</script>

<style>
</style>
