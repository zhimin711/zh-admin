import axios from '@/libs/api.request2'

const namespace = 'department'


export const getTreeDepartment = (query) => {
  return axios.request({
    url: `${namespace}/t2/`,
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
