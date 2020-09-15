import axios from '@/libs/api.request2'

const namespace = 'user'

export const getPageUser = (query) => {
  return axios.request({
    url: `${namespace}/${query.page}/${query.size}`,
    method: 'get',
    params: query.params
  })
}

export const getUserRoles = (id) => {
  return axios.request({
    url: `${namespace}/${id}/roles`,
    method: 'get'
  })
}

export const saveUserRoles = (id, data) => {
  return axios.request({
    url: `${namespace}/${id}/roles`,
    method: 'put',
    data
  })
}

export const addUser = info => {
  return axios.request({
    url: `${namespace}`,
    data: info,
    method: 'post'
  })
}

export const editUser = info => {
  return axios.request({
    url: `${namespace}/${info.id}`,
    data: info,
    method: 'put'
  })
}
