import axios from 'axios';

export default class SpotifyService {
  public search(artistName: string, albumName: string, mainAlbumOnly: boolean): Promise<any> {
    return axios.get(`api/spotify/search?artistName=${artistName}&albumName=${albumName}&mainAlbumOnly=${mainAlbumOnly}`);
  }
}
