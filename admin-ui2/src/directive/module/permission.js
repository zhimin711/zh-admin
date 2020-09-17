import store from '@/store'

function checkPermission (el, binding) {
  const { value } = binding
  const permissions = store.getters && store.getters.permissions

  if (value && value instanceof Array && value.length > 0) {
    const permissionRoles = value

    const hasPermission = permissions.some(permission => {
      return permissionRoles.includes(permission.code)
    })

    if (!hasPermission) {
      el.parentNode && el.parentNode.removeChild(el)
    }
  } else {
    throw new Error(`need roles! Like v-permission="['xxx']"`)
  }
}

export default {
  inserted (el, binding) {
    checkPermission(el, binding)
  },
  update (el, binding) {
    checkPermission(el, binding)
  }
}
