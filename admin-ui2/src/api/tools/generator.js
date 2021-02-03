import http from '@/libs/api.request2'
import fileDownload from 'js-file-download'

const namespace = 'tools/gen'

export const generateCode = (data) => {
  return http.request({
    url: `${namespace}/myBatis3`,
    method: 'post',
    data
  })
}

export function downloadZip2 (params = {}) {
  return new Promise((resolve, reject) => {
    let url = namespace + '/download'
    http.request({
      headers: {
        'Content-Type': 'application/json'
      },
      method: 'get',
      url: url, // 请求地址
      params: params, // 参数
      responseType: 'blob' // 表明返回服务器返回的数据类型
    }).then(response => {
      // const blob = new Blob([response.data], { type: 'application/zip' })
      const fileName = 'code-' + Date.parse(new Date()) + '.zip'

        fileDownload(response.data, fileName)
      // download(response.data, fileName)
      resolve(response.data)
    },
    err => {
      reject(err)
    }
    )
  })
}

export function downloadZip (params = {}) {
  let url = '/api/v1/' + namespace + '/download?filePath='+params.filePath
  window.open(encodeURI(url))
}
