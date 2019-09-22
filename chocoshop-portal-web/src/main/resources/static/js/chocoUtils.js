
function addGoodsToCart(obj, addNum) {
   var goodsId = $(obj).attr("id")
   var content = Cookies.get('shoppingCart')
   if(content == undefined) {
      content = ''
   }

   console.log('before:' + content)

   var re = new RegExp(goodsId + '/([0-9]+),')
   var result = re.exec(content)
   if(result != null) {
      var num = parseInt(result[1]) + parseInt(addNum);
      console.log(num)
      content = content.replace(re, goodsId + '/' + num + ',')
   } else {
      console.log('nu:' + content)
      content += goodsId + '/' + parseInt(addNum) + ', '
   }

   console.log('after:' + content)

   Cookies.set('shoppingCart', content)

   $('#' + goodsId + 'Toast').on('show.bs.toast', function() {
      $('#' + goodsId + 'Toast').css("display", "block")
   })

   $('#' + goodsId + 'Toast').on('hidden.bs.toast', function() {
      $('#' + goodsId + 'Toast').css("display", "none")
   })

   $('#' + goodsId + 'Toast').toast('show')
}

function deleteGoodsFromCart(goodsId) {
   var content = Cookies.get('shoppingCart')
   if(content != undefined) {
      var re = new RegExp(goodsId + '/([0-9]+),')
      content = content.replace(re, '')
      Cookies.set('shoppingCart', content)

      $('#' + goodsId).remove()
   }
   console.log(content)
}

function generateOrder() {
   $.ajax({
      type: "get",
      url: "/user/order/submit",
      async: true
   });
}


function categoryNavbar(){
   var categoryChilds = []
         $.ajax({
            type: "get",
            url: "/category/list",
            async: true,
            success: function(data) {
               $(function() {
                  var str = '';
                  for(var i = 0; i < data.length; i++) {
                     if(data[i].categoryParent == undefined) {
                        str += '<a class="nav-link card" data-toggle="collapse" data-target="#c' + data[i].categoryId + '">' + data[i].categoryName + '</a>'
                        str += '<div id="c' + data[i].categoryId + '" class="collapse" data-parent="#accordion">'
                        str += '<ul class="navbar-nav">'

                        for(var j = 0; j < data.length; j++) {
                           if(data[j].categoryParent === data[i].categoryId) {
                              str += '<li class="nav-item" style="margin-left: 20px;"><a href="/goods/result?categoryId=' + data[j].categoryId + '">' + data[j].categoryName + '</a></li>'
                              categoryChilds.push({
                                 "categoryId": data[i].categoryId,
                                 "categoryName": data[j].categoryName
                              })
                           }
                        }

                        str += '</ul>'
                        str += '</div>'
                     }
                  }
                  $('#navbarCategory').append(str)
               })
            }
         });
}
