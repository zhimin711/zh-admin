import Mock from 'mockjs'
import { login, logout, getUserInfo } from './login'
import { getTableData, getDragList, uploadImage, getOrgData, getTreeSelectData } from './data'
import { getMessageInit, getContentByMsgId, hasRead, removeReaded, restoreTrash, messageCount } from './user'

// 配置Ajax请求延时，可用来测试网络延迟大时项目中一些效果
Mock.setup({
  timeout: 1000
})

// 登录相关和获取用户信息
Mock.mock(/\/mock\/login/, login)
Mock.mock(/\/mock\/login\/user/, getUserInfo)
Mock.mock(/\/mock\/logout/, logout)
Mock.mock(/\/mock\/get_table_data/, getTableData)
Mock.mock(/\/mock\/get_drag_list/, getDragList)
Mock.mock(/\/mock\/save_error_logger/, 'success')
Mock.mock(/\/mock\/image\/upload/, uploadImage)
Mock.mock(/\/mock\/message\/init/, getMessageInit)
Mock.mock(/\/mock\/message\/content/, getContentByMsgId)
Mock.mock(/\/mock\/message\/has_read/, hasRead)
Mock.mock(/\/mock\/message\/remove_readed/, removeReaded)
Mock.mock(/\/mock\/message\/restore/, restoreTrash)
Mock.mock(/\/mock\/message\/count/, messageCount)
Mock.mock(/\/mock\/get_org_data/, getOrgData)
Mock.mock(/\/mock\/get_tree_select_data/, getTreeSelectData)

export default Mock
