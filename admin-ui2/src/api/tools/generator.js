import axios from '@/libs/api.request2'
import axios2 from 'axios'
import fileDownload from 'js-file-download'

const namespace = 'tools/gen'

export const generateCode = (data) => {
  return axios.request({
    url: `${namespace}/myBatis3`,
    method: 'post',
    data
  })
}

export function downloadZip2 (params = {}) {
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
      resolve(response.data)
      const blob = new Blob([response.data], { type: 'application/zip' })
      // console.log(blob)
      const fileName = 'code-' + Date.parse(new Date()) + '.zip'
      if (window.navigator.msSaveOrOpenBlob) {
        navigator.msSaveBlob(blob, fileName)
      } else {
        let $a = document.createElement('a')
        $a.href = window.URL.createObjectURL(blob)
        $a.download = fileName
        $a.style.display = 'none' // 隐藏标签
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

export function downloadZip (params) {
  let url = '/api/v1/' + namespace + '/download'
  axios2.get(
    url,
    {
      params,
      headers: {
        'Content-Type': 'application/json; application/octet-stream'
      },
      responseType: 'blob'
    }
  ).then((res) => {
    const data = res.data
    // const fileReader = new FileReader()

    // 进行对象装换，避免将后端接口返回的错误信息也当成文件下载
    // fileReader.onload = (event) => {
    //   try {
    //     const jsonData = JSON.parse(event.target.result);
    //     if (jsonData.status === 'fail') {
    //       // 解析成对象失败，说明是普通对象数据
    //       console.log(`下载提示： ${jsonData.message}`);
    //     }
    //   } catch (err) {
    // 解析成对象失败，说明是正常的文件流，可进行下载
    // 获取文件名
    const disposition = decodeURI(res.request.getResponseHeader('Content-Disposition'))
    console.log(disposition)
    let fileName = 'code.zip'
    const filenameRegex = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/
    const matches = filenameRegex.exec(disposition)
    if (matches !== null && matches[1]) {
      fileName = matches[1].replace(/['"]/g, '')
    }
    fileDownload(data, fileName)
    //   }
    // };
    // fileReader.readAsText(data);
  }).catch((error) => {
    // 文件下载异常处理
    console.log(error)
  })
}
