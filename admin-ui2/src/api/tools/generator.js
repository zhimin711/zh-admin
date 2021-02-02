import axios from '@/libs/api.request2'

const namespace = 'tools/gen'

export const generateCode = (data) => {
  return axios.request({
    url: `${namespace}/myBatis3`,
    method: 'post',
    data
  })
}

export function downloadZip(params = {}) {
  return new Promise((resolve, reject) => {
    let url = namespace + '/download'
    axios.request({
      headers: {
        'Content-Type': 'application/json; application/octet-stream'
      },
      method: 'get',
      url: url, // 请求地址
      params: params, // 参数
      responseType: 'blob' // 表明返回服务器返回的数据类型
    }).then(response => {
        // resolve(response.data)
        const blob = new Blob([response.data], {
          type: 'application/zip'
        });
        // console.log(blob)
        let fileName = 'code-' + Date.parse(new Date()) + '.zip'
        if (window.navigator.msSaveOrOpenBlob) {
          navigator.msSaveBlob(blob, fileName)
        } else {
          let $a = document.createElement('a')
          $a.href = window.URL.createObjectURL(blob)
          $a.download = fileName
          $a.style.display = "none"; // 隐藏标签
          $a.click()
          // 释放内存
          window.URL.revokeObjectURL($a.href)
          document.body.removeChild($a)
        }
      },
      err => {
        reject(err)
      }
    )
  })
}
