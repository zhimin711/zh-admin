import axios from '@/libs/api.request2'

const namespace = 'permission'

export const getPagePermission = (query) => {
  return axios.request({
    url: `${namespace}/${query.page}/${query.size}`,
    method: 'get',
    params: query.params
  })
}
export const getParentPermission = (query) => {
  return axios.request({
    url: `${namespace}/t/${query}`,
    method: 'get'
  })
}

export const addPermission = info => {
  return axios.request({
    url: `${namespace}`,
    data: info,
    method: 'post'
  })
}

export const editPermission = info => {
  return axios.request({
    url: `${namespace}/${info.id}`,
    data: info,
    method: 'put'
  })
}
