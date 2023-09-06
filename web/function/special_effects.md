# 色彩色差
```html
<body>
    <video autoplay muted loop class="bg-video" 
    src="https://stactus.marketing/wp-content/uploads/sactus-waves-hero.mp4"/>
    <h1 class="title">Doubt is the key to knowledge</h1>
</body>
```
```css
.bg-video{
    position:fixed;
    top:0;left:0;
    width:100%;height:100%;
    object-fit:cover;
    z-index:-1;
}
.title{
    margin-top:10vh;
    color:#fff;
    text-transform:uppercase;
    text-align:center;
    font-size: 4em;
    font-family:'Montserrat',sans-serif;
    mix-blend-mode:difference;
}
```