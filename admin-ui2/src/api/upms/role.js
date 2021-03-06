import axios from '@/libs/api.request2'

const namespace = 'role'

export const getPageRole = (query) => {
  return axios.request({
    url: `${namespace}/${query.page}/${query.size}`,
    method: 'get',
    params: query.params
  })
}

export const addRole = info => {
  return axios.request({
    url: `${namespace}`,
    data: info,
    method: 'post'
  })
}

export const editRole = info => {
  return axios.request({
    url: `${namespace}/${info.id}`,
    data: info,
    method: 'put'
  })
}

export const getAllRole = () => {
  return axios.request({
    url: `${namespace}/all/ `,
    method: 'get'
  })
}

export const getRolePermissions = (id) => {
  return axios.request({
    url: `${namespace}/${id}/permissions`,
    method: 'get'
  })
}

export const saveRolePermissions = (id, data) => {
  return axios.request({
    url: `${namespace}/${id}/permissions`,
    method: 'put',
    data
  })
}
