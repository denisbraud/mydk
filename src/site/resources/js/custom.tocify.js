/* http://gregfranko.com/jquery.tocify.js/ */
/* [DOC_STYLE_Tocify] */
$(function() {
  $('#leftColumn').append('<div id="toc"><b>'+$('#navcolumn strong').first().text()+'</b></div>');
  var baseUrl = window.location.href.split('#')[0];
  var toc = $("#toc").tocify({
    selectors: "h2,h3,h4,h5,h6,h7",
    hashGenerator: function(text, element) {
      var $elem = $(element);
      var customId = $elem.parent('.section').attr('id');
      if (customId) {
        return customId;
      }
      return $elem.children('a').eq(0).attr('name');
    }
  }).data("toc-tocify");
  $('#toc a').each(function(){
    var $a = $(this);
    var txt = $a.text();
    var href = baseUrl;
    var anchor = $a.parent().attr("data-unique");
    if (anchor) {
      href += '#' + anchor;
    }
    $a.attr('href', href);
    if (txt.length > 43) {
      $a.text(txt.substring(0,40)+"...");
    }
 });
});
