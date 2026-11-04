const { defineConfig } = require('@vue/cli-service')

module.exports = defineConfig({
  transpileDependencies: true,
  lintOnSave: false,
  devServer: {
    port: 3008,
    // 开发环境统一走代理，前端只请求 /API/**，避免跨域与 baseURL 写死
    proxy: {
      '/API': {
        target: 'http://localhost:8080',
        changeOrigin: true
      }
    }
  }
})
