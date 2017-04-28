"use strict";

(function (w, d) {
    hljs.initHighlightingOnLoad()
    w.onload = function() {
        const href = d.location.href
        const v = Array.from(d.querySelectorAll('ul.nav a'))
              .filter((a) => href.endsWith(a.getAttribute("href")))
              .map((a) => a.parentNode)
        v.push(d.querySelector('ul.nav li'))
        v[0].className = "active"
    }
})(window, document);

(function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
(i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
})(window,document,'script','https://www.google-analytics.com/analytics.js','ga');

ga('create', 'UA-83622197-1', 'auto');
ga('send', 'pageview');
