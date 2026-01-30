const fs   = require('fs')
const path = require('path')

const  input_filepath = path.resolve(process.argv[2])
const output_filepath = path.resolve(process.argv[3])

const rtmp_urls = fs.readFileSync(input_filepath, {encoding: 'utf8'})
  .split(/[\r\n]+/g)
  .filter(url => !!url && url.startsWith('rtmp:'))

const rtmp_url_title_regex = new RegExp('^.*/([^/]+)\\.stream.*$')

const video_streams = rtmp_urls.map((url, index) => {
  const title = 'CA DOT: ' + url.replace(rtmp_url_title_regex, '$1').replace(/[-_]+/g, ' ')

  return {title, URL_low_res: url, URL_high_res: null, is_enabled: (index <= 3)}
})

fs.writeFileSync(output_filepath, JSON.stringify(video_streams, null, 2), {encoding: 'utf8'})
