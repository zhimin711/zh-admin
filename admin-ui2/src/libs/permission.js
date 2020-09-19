import store from '@/store'

/**
 * @param {Array} value
 * @returns {Boolean}
 * @example see @/views/permission/directive.vue
 */
export default function checkPermission (value) {
  const permissionCodes = value && typeof value === 'string' ? [value] : value

  if (permissionCodes && permissionCodes instanceof Array && permissionCodes.length > 0) {
    const permissions = store.getters && store.getters.permissions

    const hasPermission = permissions.some(permission => {
      return permissionCodes.includes(permission.code)
    })
    return hasPermission
  } else {
    return false
  }
}
