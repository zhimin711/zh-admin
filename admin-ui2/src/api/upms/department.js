import axios from '@/libs/api.request2'

const namespace = 'department'

export const getTreeDepartment = (query) => {
  return axios.request({
    url: `${namespace}/t2/${query}`,
    method: 'get'
  })
}

export const getDepartment = (query) => {
  return axios.request({
    url: `${namespace}/${query}`,
    method: 'get'
  })
}

export const addDepartment = info => {
  return axios.request({
    url: `${namespace}`,
    data: info,
    method: 'post'
  })
}

export const editDepartment = info => {
  return axios.request({
    url: `${namespace}/${info.id}`,
    data: info,
    method: 'put'
  })
}

export const delDepartment = id => {
  return axios.request({
    url: `${namespace}/${id}`,
    method: 'delete'
  })
}

export const getDepartmentPositions = (id) => {
  return axios.request({
    url: `${namespace}/${id}/positions`,
    method: 'get'
  })
}

export const saveDepartmentPositions = (id, data) => {
  return axios.request({
    url: `${namespace}/${id}/positions`,
    method: 'put',
    data
  })
}
