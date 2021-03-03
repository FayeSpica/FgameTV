import requests, time,re
import demjson

def get_stream_list(room_id):
  url = f"https://egame.qq.com/{room_id}"

  headers = {
      'authority': "egame.qq.com",
      'cache-control': "max-age=0,no-cache",
      'upgrade-insecure-requests': "1",
      'user-agent': "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.142 Safari/537.36",
      'dnt': "1",
      'accept': "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3",
      'referer': "https://egame.qq.com/",
      'accept-encoding': "gzip, deflate, br",
      'accept-language': "zh-CN,zh;q=0.9,en;q=0.8",
      'Host': "egame.qq.com",
      }

  response = requests.request("GET", url, headers=headers)
  pattern = re.compile(r'"live-info":{(.+?),streamInfos:')
  stream_list = [ i for i in re.findall(r'streamInfos:\[(.+?)\]',response.text)]
  streamInfos = '['+stream_list[0]+']'
  return str(streamInfos)


def get_pid(room_id):
  res = []
  try:
    url = f"https://egame.qq.com/{room_id}"

    headers = {
        'authority': "egame.qq.com",
        'cache-control': "max-age=0,no-cache",
        'upgrade-insecure-requests': "1",
        'user-agent': "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/75.0.3770.142 Safari/537.36",
        'dnt': "1",
        'accept': "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3",
        'referer': "https://egame.qq.com/",
        'accept-encoding': "gzip, deflate, br",
        'accept-language': "zh-CN,zh;q=0.9,en;q=0.8",
        'Host': "egame.qq.com",
        }

    response = requests.request("GET", url, headers=headers)
    res = re.findall(r'pid\":\"(.+?)\"}',response.text)[0]
  finally:
    return str(res)


def get_danmaku(anchor_id,vid,last_tm):
  res = []
  try:
    danmaku_url = 'http://wdanmaku.egame.qq.com/cgi-bin/pgg_barrage_async_fcgi'
    #"scenes":4096,
    danmaku_head_data = {
              'param': '{"key":{"module":"pgg_live_barrage_svr","method":"get_barrage","param":{"anchor_id":'+str(anchor_id)+',"vid":"'+str(vid)+'","last_tm":'+str(last_tm)+'}}}',
              'app_info': '{"platform":4,"terminal_type":2,"egame_id":"egame_official","version_code":"9.9.9.9","version_name":"9.9.9.9"}',
              'tt': '1'
              }
    print(danmaku_head_data)
    response = requests.get(danmaku_url, params = danmaku_head_data)
    response.encoding = 'utf-8'
    #print(response.json())
    res = response.json()['data']['key']['retBody']['data']
  finally:
    return str(res)


def get_live_list(app_id,page_num,page_size):
  res = []
  try:
    live_list_url='https://share.egame.qq.com/cgi-bin/pgg_live_async_fcgi'
    live_list_data = {
              'param': '{"key":{"module":"pgg_live_read_ifc_mt_svr","method":"get_pc_live_list","param":{"appid":"'+app_id+'","page_num":'+str(page_num)+',"page_size":'+str(page_size)+',"tag_id":0,"tag_id_str":""}}}',
              'app_info': '{"platform":4,"terminal_type":2,"egame_id":"egame_official","version_code":"9.9.9.9","version_name":"9.9.9.9","ext_info":{"_qedj_t":"","ALG-flag_type":"","ALG-flag_pos":""}',
              'tt': '1'
              }
    #print(live_list_data)
    response = requests.get(live_list_url, params = live_list_data)
    response.encoding = 'utf-8'
    #print(response.json())
    res = response.json()['data']['key']['retBody']['data']['live_data']['live_list']
  finally:
    return str(res) 


def get_layout_list():
  res = []
  try:
    game_list_url='https://egame.qq.com/gamelist'
    response = requests.get(game_list_url,{})
    response.encoding = 'utf-8'
    _list = re.findall(r'/livelist\?layoutid=(.+?)\".+?title=\"(.+?)\".+?>',response.text)
    layout_list = []
    for _tuple in _list:
        layout_list.append({"id":_tuple[0],"title":_tuple[1]})
    res = layout_list
  finally:
    return str(res) 