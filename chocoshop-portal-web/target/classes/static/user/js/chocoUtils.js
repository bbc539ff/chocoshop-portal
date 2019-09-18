
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