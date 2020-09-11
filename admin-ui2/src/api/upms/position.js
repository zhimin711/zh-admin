import axios from '@/libs/api.request2'

const namespace = 'position'

export const getPagePosition = (query) => {
  return axios.request({
    url: `${namespace}/${query.page}/${query.size}`,
    method: 'get',
    params: query.params
  })
}

export const addPosition = info => {
  return axios.request({
    url: `${namespace}`,
    data: info,
    method: 'post'
  })
}

export const editPosition = info => {
  return axios.request({
    url: `${namespace}/${info.id}`,
    data: info,
    method: 'put'
  })
}
