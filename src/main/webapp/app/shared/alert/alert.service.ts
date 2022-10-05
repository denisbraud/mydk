import Vue from 'vue';

export default class AlertService {
  public show(variant: string, instance: Vue, message: string, params?: any) {
    const alertMessage = instance.$t(message, params);
    (instance.$root as any).$bvToast.toast(alertMessage.toString(), {
      toaster: 'b-toaster-top-center',
      title: 'My Discotek',
      variant: variant,
      solid: true,
      autoHideDelay: 5000,
    });
  }
  public showError(instance: Vue, message: string, params?: any) {
    this.show('danger', instance, message, params);
  }
  public showInfo(instance: Vue, message: string, params?: any) {
    this.show('info', instance, message, params);
  }
  public showHttpInfo(instance: Vue, res: any) {
    const message = res.headers['x-mydkapp-alert'];
    const params = { param: decodeURIComponent(res.headers['x-mydkapp-params'].replace(/\+/g, ' ')) };
    this.show('info', instance, message, params);
  }
  public showHttpError(instance: Vue, httpErrorResponse: any) {
    switch (httpErrorResponse.status) {
      case 0:
        this.showError(instance, 'error.server.not.reachable');
        break;
      case 400: {
        const arr = Object.keys(httpErrorResponse.headers);
        let errorHeader: string | null = null;
        let entityKey: string | null = null;
        for (const entry of arr) {
          if (entry.toLowerCase().endsWith('app-error')) {
            errorHeader = httpErrorResponse.headers[entry];
          } else if (entry.toLowerCase().endsWith('app-params')) {
            entityKey = httpErrorResponse.headers[entry];
          }
        }
        if (errorHeader) {
          const alertData = entityKey ? { entityName: instance.$t(`global.menu.entities.${entityKey}`) } : undefined;
          this.showError(instance, errorHeader, alertData);
        } else if (httpErrorResponse.data !== '' && httpErrorResponse.data.fieldErrors) {
          this.showError(instance, httpErrorResponse.data.message);
        } else {
          this.showError(instance, httpErrorResponse.data.message);
        }
        break;
      }
      case 404:
        this.showError(instance, 'error.http.404');
        break;
      default:
        this.showError(instance, httpErrorResponse.data.message);
    }
  }
}
