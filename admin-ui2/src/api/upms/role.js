import axios from '@/libs/api.request2'

const namespace = 'role'

export const getPageUser = (query) => {
  return axios.request({
    url: `${namespace}/${query.page}/${query.pageSize}`,
    method: 'get',
    params: query.params
  })
}
