const path = require('path');
const webpack = require('webpack');
const HTMLPlugin = require('html-webpack-plugin');
const ExtractPlugin = require('extract-text-webpack-plugin');
const isDev = process.env.NODE_ENV === 'development'

const config = {
    target: 'web',
    entry: path.join(__dirname, 'src/index.js'),
    output: {
        filename: 'bundle.[hash:8].js',
        path: path.join(__dirname, 'dist'),
    },
    module: {
        rules: [
            {
                test: /\.vue$/,
                loader: 'vue-loader'
            },
            {
                test: /\.jsx/,
                loader: 'babel-loader'
            },
            {
                test: /\.(gif|jpg|jpeg|png|svg|woff|woff2|eot|ttf)$/,
                use: [
                    {
                        loader: 'url-loader',//封装了file-loader
                        options: {
                            limit: 1024,//小于该值会转为base64
                            name: '[name].[ext]'//文件名
                        }
                    }
                ]

            }
        ]
    },
    plugins: [
        new webpack.DefinePlugin({//加入该插件后可以在代码里通过process，在开发环境会有错误提醒，当然文件会变大，生产环境则相反
            'process.env': {
                NODE_ENV: isDev ? '"development"' : '"production"'
            }
        }),
        new HTMLPlugin()//该插件用于生成入口index.html页面
    ]
}

if (isDev) {
    config.devServer = {
        port: '8000',
        host: '0.0.0.0',//这样设置既可以用127.0.0.1也可用ip访问，方便手机或其他机器测试
        overlay: {//webpack编译错误显示到网页
            errors: true
        },
        hot: true,//热加载，改动一个组件后只是局部刷新，不会刷新整个页面，配合HotModuleReplacementPlugin与HotModuleReplacementPlugin使用
        //historyFallback:{},//其他地址映射到index.html
        //open:true//启动后自动打开浏览器
    }
    config.devtool = '#cheap-module-eval-source-map'//将es6,vue等js代码source-map为方便调试的代码
    config.plugins.push(//配合devServer热加载
        new webpack.HotModuleReplacementPlugin(),
        new webpack.NoEmitOnErrorsPlugin()
    );
    config.module.rules.push(//测试环境使用内嵌css
        {
            test: /\.css$/,
            use: [//loader的处理顺序是stylus-loader->...->style-loader
                'style-loader',//包一层js代码，内嵌入html页面
                'css-loader',
                'postcss-loader'

            ]
        });

}else{
    config.entry={
        app:path.join(__dirname, 'src/index.js'),
        vender:['vue']//将类库文件抽取出来,配合webpack.optimize.CommonsChunkPlugin使用
    }
    config.output.filename='[name].[chunkhash:8].js';//使用chunkhash而不是hash，因为app.js的改变只改变app.js的hash,vendor.js的hash不发生变化
    config.module.rules.push(//生产环境使用引用css
        {
            test: /\.css$/,
            use: ExtractPlugin.extract({
                fallback:'style-loader',
                use:['css-loader',//内嵌入html页面
                    'postcss-loader'
                ]
            })


        });
    config.plugins.push(
        new ExtractPlugin('styles.[contentHash:8].css'),//将css抽取为外部文件
        new webpack.optimize.CommonsChunkPlugin({name:'vendor'}),//将类库文件抽取出来,类库因为不变动可以利用浏览器缓存
        new webpack.optimize.CommonsChunkPlugin({name:'runtime'})//将webpack相关js抽取出来
    );
}

module.exports = config

// plugins: [
//     new webpack.optimize.CommonsChunkPlugin('common.js'),
//     new webpack.ProvidePlugin({
//         jQuery: "jquery",
//         $: "jquery"
//     })
// ]
