<template>
  <div class="app-container" style="width: 600px;">
    <el-form ref="form" :rules="rules" :model="form" label-width="120px">
      <el-form-item label="密钥" prop="oldPassword">
        <el-input v-model="form.oldPassword" placeholder="加密Key" type="text" style="width: 200px;" />
      </el-form-item>
      <el-form-item label="密码" prop="password">
        <el-input v-model="form.password" placeholder="明文密码（字符串）" type="text" style="width: 200px;" />
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="onSubmit">加密</el-button>
        <el-button @click="onCancel">关闭</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script>
import { encrypt } from '@/api/tools'

export default {
  data() {
    return {
      form: {
        oldPassword: 'abc123',
        password: null
      },
      rules: {
        password: [{ required: true, message: '密码不能为空', trigger: 'change' }],
        oldPassword: [{ required: true, message: '密钥不能为空', trigger: 'change' }]
      }
    }
  },
  created() {
  },
  methods: {
    onSubmit() {
      this.$refs['form'].validate((valid) => {
        if (valid) {
          encrypt(this.form).then(res => {
            if (res.data) {
              // this.form.oldPassword = ''
              // this.form.password = null
              // this.$nextTick(() => {
              //   this.$refs['form'].clearValidate()
              // })
              this.$alert(`ENC(${res.data})`, '加密信息成功', {
                confirmButtonText: '关闭',
                callback: action => {
                  const url = `ENC(${res.data})`
                  const oInput = document.createElement('input')
                  oInput.value = url
                  document.body.appendChild(oInput)
                  oInput.select() // 选择对象;
                  console.log(oInput.value)
                  document.execCommand('Copy') // 执行浏览器复制命令
                  this.$message({
                    message: '信息已复制',
                    type: 'success'
                  })
                  oInput.remove()
                }
              })
            } else {
              this.$message({
                message: '加密信息失败',
                type: 'error'
              })
            }
          })
        }
      })
    },
    onCancel() {
      history.go(-1)
    }
  }
}
</script>

<style scoped>
.line{
  text-align: center;
}
</style>

