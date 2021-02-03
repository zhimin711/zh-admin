import axios2 from "axios";
import fileDownload from "js-file-download";

export function downloadZip2 (params) {
  let url = '/api/v1/' + namespace + '/download'
  axios2.get(
    url,
    {
      params,
      headers: {
        'Content-Type': 'application/json'
      },
      responseType: 'arraybuffer'
    }
  ).then((res) => {
    const data = res.data

    try {
      const jsonData = JSON.parse(data)
      if (!jsonData.success) {
        // 解析成对象失败，说明是普通对象数据
        console.log(`下载提示： ${jsonData.message}`)
      }
      return
    } catch (err) {
      // 解析成对象失败，说明是正常的文件流，可进行下载
    }
    const blob = new Blob([res.data], { type: 'application/octet-stream;charset=utf-8' })
    let fileReader = new FileReader()

    // 进行对象装换，避免将后端接口返回的错误信息也当成文件下载
    fileReader.onload = (event) => {
      // 获取文件名
      const disposition = decodeURI(res.request.getResponseHeader('Content-Disposition'))
      let fileName = 'code-' + Date.parse(new Date()) + '.zip'
      const filenameRegex = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/
      const matches = filenameRegex.exec(disposition)
      if (matches && matches[1]) {
        fileName = matches[1].replace(/['"]/g, '')
      }
      fileDownload(res.data, fileName)
      // download(res.data, fileName)
    }
    fileReader.readAsDataURL(blob)
  }).catch((error) => {
    // 文件下载异常处理
    console.log(error)
  })
}

function download (fileData, fileName) {
  const URL = window.URL || window.webkitURL
  const url = URL.createObjectURL(new Blob([fileData]))
  if (window.navigator.msSaveOrOpenBlob) {
    navigator.msSaveBlob(blob, fileName)
  } else {
    const link = document.createElement('a')
    link.href = url
    link.setAttribute('download', fileName) // 下载文件的名称及文件类型后缀
    document.body.appendChild(link)
    link.click()
    document.body.removeChild(link) // 下载完成移除元素
    URL.revokeObjectURL(url) // 释放掉blob对象
  }
}
