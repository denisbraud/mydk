$(document).ready(function() {
  var resourceName = document.location.pathname.split("/").slice(-1).join();
  if (resourceName.indexOf("MyDiscotek_") != -1) {
    $("#bannerLeft").attr('href', resourceName);
    $("#breadcrumbs .xleft a").hide();
    $("#toc").css("margin", "0")
    $("#toc b").hide()
    $("#leftColumn").css("border","none").css("background-color","#fff");
    $("#navcolumn").hide();
  }
});
