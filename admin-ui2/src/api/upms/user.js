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
