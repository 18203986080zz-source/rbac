import request from './request'

export function getEmployeePage(current = 1) {
  return request.get(`/employee/page/${current}`)
}

export function createEmployee(data) {
  return request.post('/employee', data)
}

export function updateEmployee(data) {
  return request.put('/employee', data)
}

export function deleteEmployee(ids) {
  return request.delete('/employee', { params: { ids } })
}

export function getCompanyList() {
  return request.get('/company/list')
}
