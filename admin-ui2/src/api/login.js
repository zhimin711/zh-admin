import axios from '@/libs/api.request2'

export const login = ({ username, password }) => {
  const data = {
    username,
    password
  }
  return axios.request({
    url: 'login',
    data,
    method: 'post'
  })
}

export const getUserInfo = (token) => {
  return axios.request({
    url: 'login/user',
    params: {
      token
    },
    method: 'get'
  })
}

export const getUserMenus = (token) => {
  return axios.request({
    url: 'login/menus',
    params: {
      token
    },
    method: 'get'
  })
}

export const getUserPermissions = (token) => {
  return axios.request({
    url: 'login/permissions',
    params: {
      token
    },
    method: 'get'
  })
}

export const logout = (token) => {
  return axios.request({
    url: 'logout',
    method: 'post'
  })
}
