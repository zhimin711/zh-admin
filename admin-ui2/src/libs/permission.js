import store from '@/store'

/**
 * @param {Array} value
 * @returns {Boolean}
 * @example see @/views/permission/directive.vue
 */
export default function checkPermission (value) {
  if (value && value instanceof Array && value.length > 0) {
    const permissions = store.getters && store.getters.permissions
    const permissionCodes = value

    const hasPermission = permissions.some(permission => {
      return permissionCodes.includes(permission.code)
    })
    return hasPermission
  } else {
    return false
  }
}
