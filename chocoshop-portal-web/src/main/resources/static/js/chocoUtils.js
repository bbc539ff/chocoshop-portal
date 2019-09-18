// ex: paginationConfig('admin', function, function)
function paginationConfig(obj, initEditFn, doDel) {
   var pager = $('#dg').datagrid().datagrid('getPager'); // get the pager of datagrid
   pager.pagination({
      buttons: [{
         iconCls: 'icon-search',
         handler: function() {
            $('#addWin').form('clear');
            $('#aSmt').attr("onclick", "searh('" + obj + "', initEditFn, doDel)")
            $('#addWin').window('open');
         }
      }, {
         iconCls: 'icon-add',
         handler: function() {
            $('#addWin').form('clear');
            $('#aSmt').attr("onclick", "doAddEdit('" + obj + "', 'add')")
            $('#addWin').window('open');

         }
      }, {
         iconCls: 'icon-edit',
         handler: function() {
            var row = $('#dg').datagrid('getSelected');
            if(row) {
               $('#aSmt').attr("onclick", "doAddEdit('" + obj + "', 'update')")
               initEditFn(row)
               $('#addWin').window('open');
            } else {
               $.messager.alert('Warning', '请先选择！');
            }

         }
      }, {
         iconCls: 'icon-remove',
         handler: function() {
            doDel()
         }
      }]
   });
}

// ajax 提交表单 add/edit
function doAddEdit(obj, oper) {
   var form = $("#formAddEdit").serializeArray();
   console.log(form)
   var formData = new FormData()
   for(var i = 0; i < form.length; i++) {
      console.log(form[i])
      if(form[i].value.trim() != "") {
         formData.append(form[i].name, form[i].value)
         console.log(form[i].name)
      }
   }

   $.ajax({
      url: '/admin/' + obj + '-info/' + oper,
      type: 'POST',
      cache: false,
      data: new FormData($('#formAddEdit')[0]),
      processData: false,
      contentType: false
   }).done(function(res) {
      $.messager.alert('Warning', '操作成功！' + oper);
      $('#addWin').window('close');
      $('#dg').datagrid('reload')
   }).fail(function(res) {
      $.messager.alert('Warning', '操作失败！' + oper);
      $('#addWin').window('close');
   });
}

function searh(obj, initEditFn, doDel) {
   var form = $("#formAddEdit")
   var conf = serializeNotNull(form.serialize())

   $('#dg').datagrid({
      url: '/admin/' + obj + '-info/search?' + conf,
      rownumbers: true,
      singleSelect: true,
      pagination: true,
   })
   paginationConfig(obj, initEditFn, doDel)
   $('#addWin').window('close');
}

// java Date 字符串转  easyui 日期格式
function parseDate(value) {
   var date = new Date(value);
   var year = date.getFullYear();
   var month = date.getMonth() + 1; //月份+1   
   var day = date.getDate();
   var hour = date.getHours();
   var minutes = date.getMinutes();
   var second = date.getSeconds();
   return day + "/" + month + "/" + year + " " + hour + ":" + minutes + ":" + second;
}

function serializeNotNull(serStr) {
   return serStr.split("&").filter(str => !str.endsWith("=")).join("&");
}

function dateFormatter(value, index, row) {
   var returnVal = value.replace('T', ' ')
   var returnVal = returnVal.split(".")[0]
   return returnVal
}