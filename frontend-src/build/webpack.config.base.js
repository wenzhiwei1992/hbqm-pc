const path = require('path');

const isDev = process.env.NODE_ENV === 'development'
function resolve (dir) {
    return path.join(__dirname, '..', dir)
}
const config = {
    target: 'web',
    entry: path.join(__dirname, '../client/index.js'),
    output: {
        filename: 'bundle.[hash:8].js',
        path: path.join(__dirname, '../dist'),
    },
    resolve: {
        alias: {
            '@': resolve('client'),
            'vue$': 'vue/dist/vue.esm.js' 
        }
    },
    module: {
        rules: [
            {
                test: /\.vue$/,
                loader: 'vue-loader'
            },
            {
                test: /\.js$/,
                loader: 'babel-loader',
                exclude:/node_modules/
            },
            {
                test: /\.jsx$/,
                loader: 'babel-loader'
            },
            {
                test: /\.(gif|jpg|jpeg|png|svg|woff|woff2|eot|ttf|yml)$/,
                use: [
                    {
                        loader: 'url-loader',//封装了file-loader
                        options: {
                            limit: 1024,//小于该值会转为base64
                            name: 'resources/[path][name].[hash:8].[ext]'//文件名
                        },

                    }
                ],
                // exclude: [
                //     path.resolve(__dirname, '../client/icons'),
                // ],

            },
            // {
            //     test: /\.svg$/,
            //     loader: 'svg-sprite-loader',
            //     include: [
            //         path.resolve(__dirname, '../client/icons'),
            //     ],
            // }
        ]
    }
}


module.exports = config