(function ($) {
    hljs.initHighlightingOnLoad()
    $(function() {
      const href = document.location.href
      const v = $('ul.nav a').filter(function(idx, a) {
        return href.endsWith(a.attributes["href"].value)
      }).parent()
      v.push($('ul.nav li:first'))
      $(v[0]).addClass("active")
    })
})(jQuery)
