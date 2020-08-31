import request from '@/utils/request'

export function encrypt(data) {
  return request({
    url: '/tools/encrypt',
    method: 'put',
    data
  })
}
