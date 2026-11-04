import request from './request'

export function login(data) {
  return request.post('/login', data)
}

export function getUserPage(current = 1) {
  return request.get(`/user/page/${current}`)
}

export function getDataPage(current = 1) {
  return request.get(`/data/page/${current}`)
}
