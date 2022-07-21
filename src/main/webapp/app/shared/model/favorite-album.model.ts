export interface IFavoriteAlbum {
  id?: number;
  login?: string;
  albumSpotifyId?: string;
  rank?: number;
  comment?: string | null;
  album?: any | null;
}

export class FavoriteAlbum implements IFavoriteAlbum {
  constructor(
    public id?: number,
    public login?: string,
    public albumSpotifyId?: string,
    public rank?: number,
    public comment?: string | null,
    public album?: any | null
  ) {}
}
