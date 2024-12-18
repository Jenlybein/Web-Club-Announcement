// pinia 持久化实现

const TokenKey = 'vue_admin_template_token'

export function getToken() {
  return localStorage.getItem(TokenKey)
}

export function setToken(token) {
  localStorage.setItem(TokenKey, token)
}

export function removeToken() {
  localStorage.removeItem(TokenKey)
}

const NameKey = 'vue_admin_template_nickname'

export function getName() {
  return localStorage.getItem(NameKey)
}

export function setName(name) {
  localStorage.setItem(NameKey, name)
}

export function removeName() {
  localStorage.removeItem(NameKey)
}

const UidKey = 'vue_admin_template_uid'

export function getUid() {
  return localStorage.getItem(UidKey)
}

export function setUid(uid) {
  localStorage.setItem(UidKey, uid)
}

export function removeUid() {
  localStorage.removeItem(UidKey)
}