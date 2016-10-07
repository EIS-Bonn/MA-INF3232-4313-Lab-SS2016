const debug = process.env.NODE_ENV !== 'production';

const webpack = require('webpack');
const path = require('path');

const Dashboard = require('webpack-dashboard');
const DashboardPlugin = require('webpack-dashboard/plugin');

module.exports = {
  context: path.join(__dirname, ''),
  devtool: debug ? 'cheap-module-eval-source-map' : '',
  entry: './src/js/client.js',
  output: {
    filename: "bundle.js",
    path: __dirname + "/"
  },
  module: {
    preLoaders: [
      {
        test: /\.jsx?$/,
        include: [new RegExp(path.join(__dirname, 'src'))],
        loader: 'eslint'
      }
    ],
    loaders: [
      {
        test: /\.jsx?$/,
        exclude: /node_modules/,
        loader: 'babel',
        query: {
          presets: ['es2015', 'react', 'stage-0'],
          plugins: ['react-html-attrs', 'transform-class-properties', 'transform-decorators-legacy'],
        }
      },
      {
        test: /\.scss$/,
        exclude: /node_modules/,
        loader: 'style!css!sass'
      },
      {
        test: /\.css$/,
        exclude: /node_modules/,
        loader: 'style!css'
      }
    ]
  },
  externals: {
    'react/lib/ExecutionEnvironment': true,
    'react/lib/ReactContext': true,
    'react/addons': true
  },
  resolve: {
    extensions: ['', '.js'],
  },
  eslint: {
    configFile: '.eslintrc'
  },
  plugins: debug ? [
    new DashboardPlugin(new Dashboard().setData)
  ] : [
    new webpack.DefinePlugin({
      'process.env': {
        NODE_ENV: '"production"'
      }
    }),
    new webpack.optimize.DedupePlugin(),
    new webpack.optimize.OccurenceOrderPlugin(),
    new webpack.optimize.UglifyJsPlugin({ mangle: false, sourcemap: false, compress: { screw_ie8: true, warnings: false } }),
  ],
  devServer: {
    quiet: true,
    historyApiFallback: true,
    port: 3333
  },
};
