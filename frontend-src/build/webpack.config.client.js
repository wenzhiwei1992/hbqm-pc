const path = require('path');
const webpack = require('webpack');
const merge= require('webpack-merge');
const HTMLPlugin = require('html-webpack-plugin');
const ExtractTextWebpackPlugin=require('extract-text-webpack-plugin')
const { VueLoaderPlugin } = require('vue-loader')
const TerserWebpackPlugin = require('terser-webpack-plugin')
const OptimizeCssAssetsWebpackPlugin = require('optimize-css-assets-webpack-plugin')
const MiniCssExtractPlugin = require("mini-css-extract-plugin");
const UglifyJSPlugin = require('uglifyjs-webpack-plugin');
const CompressionPlugin = require("compression-webpack-plugin");
const baseConfig=require('./webpack.config.base');

const isDev = process.env.NODE_ENV === 'development'

const defaultPlugins=[
    new webpack.DefinePlugin({//加入该插件后可以在代码里通过process，在开发环境会有错误提醒，当然文件会变大，生产环境则相反
        'process.env': {
            NODE_ENV: isDev ? '"development"' : '"production"'
        }
    }),
    new HTMLPlugin({
        template:path.join(__dirname,'template.html')
    }),//该插件用于生成入口index.html页面
    new VueLoaderPlugin()
]

const devServer = {
    port: '8000',
    host: '0.0.0.0',//这样设置既可以用127.0.0.1也可用ip访问，方便手机或其他机器测试
    overlay: {//webpack编译错误显示到网页
        errors: true
    },
    hot: true,//热加载，改动一个组件后只是局部刷新，不会刷新整个页面，配合HotModuleReplacementPlugin与HotModuleReplacementPlugin使用
    // historyApiFallback:{
    //     index:'/index.html'
    // }//其他地址映射到index.html
    //open:true//启动后自动打开浏览器
    disableHostCheck: true,
    //progress: true,
    compress:true,
    stats: { warningsFilter: (warning) => /Conflicting order between/gm.test(warning) ,children: false}
}

let config

if (isDev) {
    config=merge(baseConfig,{
        module:{
            rules:[
                {
                  test: /\.css$/,
                  use: [
                    MiniCssExtractPlugin.loader,
                    'css-loader',
                    {
                      loader:'postcss-loader',
                      options:{
                        ident:'postcss',
                        plugins:()=>{
                          require('postcss-preset-env')()
                        }
                      }
                    }
                  ]
                },
                {
                  test: /\.scss$/,
                  use: [//loader的处理顺序是stylus-loader->...->style-loader
                      'style-loader',//包一层js代码，内嵌入html页面
                      'css-loader',
                      'sass-loader',
                  ]
                }
            ]
        },
        devServer,
        // devtool: '#cheap-module-eval-source-map',//将es6,vue等js代码source-map为方便调试的代码
        plugins:defaultPlugins.concat([
            new webpack.HotModuleReplacementPlugin(),
            new webpack.NoEmitOnErrorsPlugin(),
            new MiniCssExtractPlugin({
              filename: "[name].css",
              chunkFilename: "styles.[contentHash:8].css"
            }),
        ])
    })
}else{
    config=merge(baseConfig,{
        entry:{
            app:path.join(__dirname, '../client/index.js'),
            vender:['vue']//将类库文件抽取出来,配合webpack.optimize.CommonsChunkPlugin使用
        },
        output:{
            filename:'[name].[chunkhash:8].js'
        },
        module:{
            rules:[
                {
                    // test: /\.css$/,
                    // use: ExtractPlugin.extract({
                    //     fallback:'style-loader',
                    //     use:['css-loader',//内嵌入html页面
                    //         'postcss-loader'
                    //     ]
                    // })
                    test: /\.css$/,
                    use: [
                      MiniCssExtractPlugin.loader,
                      'css-loader',
                      {
                        loader:'postcss-loader',
                        options:{
                          ident:'postcss',
                          plugins:()=>{
                            require('postcss-preset-env')()
                          }
                        }
                      }
                    ]
                },
                {
                    test: /\.scss$/,
                    use:[
                      MiniCssExtractPlugin.loader,
                      'css-loader',
                      'sass-loader'
                    ]
                }
            ]
        },
        plugins:defaultPlugins.concat([
            // new ExtractPlugin('styles.[contentHash:8].css'),//将css抽取为外部文件
            // new webpack.optimize.CommonsChunkPlugin({name:'vendor'}),//将类库文件抽取出来,类库因为不变动可以利用浏览器缓存
            // new webpack.optimize.CommonsChunkPlugin({name:'runtime'}),//将webpack相关js抽取出来
            new MiniCssExtractPlugin({
              filename: "[name].css",
              chunkFilename: "styles.[contentHash:8].css"
            }),
            // 压缩css
            new OptimizeCssAssetsWebpackPlugin(),
            new CompressionPlugin({
                asset: '[path].gz[query]', //目标资源名称。[file] 会被替换成原资源。[path] 会被替换成原资源路径，[query] 替换成原查询字符串
                algorithm: 'gzip',//算法
                test: new RegExp(
                '\\.(js|css)$'  //压缩 js 与 css
                ),
                threshold: 10240,//只处理比这个值大的资源。按字节计算
                minRatio: 0.8//只有压缩率比这个值小的资源才会被处理
            })
        ]),
        optimization: {
          splitChunks: {
            chunks: 'all',
            cacheGroups: {
              // 分割chunk的组
              // node_modules文件会被打包到 vendors 组的chunk中。--> vendors~xxx.js
              // 满足上面的公共规则，如：大小超过30kb，至少被引用一次。
              vendors: {
                test: /[\\/]node_modules[\\/]/,
                // 优先级
                priority: -10
              },
              default: {
                // 要提取的chunk最少被引用2次
                minChunks: 2,
                // 优先级
                priority: -20,
                // 如果当前要打包的模块，和之前已经被提取的模块是同一个，就会复用，而不是重新打包模块
                reuseExistingChunk: true
              } 
            }
          },
          minimizer: [
            // 配置生产环境的压缩方案：js和css
            new TerserWebpackPlugin({
              // 开启缓存
              cache: true,
              // 开启多进程打包
              parallel: true,
              // 启动source-map
              sourceMap: true
            })
          ],
          runtimeChunk: {
            name: entrypoint => `runtime-${entrypoint.name}`
          }
        }
    });
}

module.exports = config