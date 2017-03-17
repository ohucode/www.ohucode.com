(function ($) {
    hljs.initHighlightingOnLoad()
    $(function() {
      const href = document.location.href
      $('ul.nav a').filter(function(idx, a) {
        return href.endsWith(a.attributes["href"].value)
      }).parent().addClass("active")
    })
})(jQuery)
