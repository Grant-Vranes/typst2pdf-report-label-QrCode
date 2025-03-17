#import "./tiaoma-0.2.0/lib.typ":barcode

#let data = json.decode("${coupletTemplateJson}")
 
#set page(
height: 50mm,
width: 80mm,
margin: (x: 0mm, y: 0mm)
)

#set rect(
  inset: 0pt,
  fill: white,
  width: 100%
)

#let index = 0

#for fc in data.qrList {
  index = index + 1
  grid(
    columns: (80mm),
    rect(width: 100%)[
      #block(
        height: 9.92mm,
        width: 100mm,
        below: 0em
      )
      #set align(center)
      #barcode(fc, "QRCode", height: 30.16mm, width: 30.23mm)
    ]
  )
  block(
    width: 80mm,
    height: auto,
    below: 0em,
    text()[
      #set align(center + horizon)
      #set text(font: "Sans CHS", size: 8pt, weight: "bold")
      #fc
    ]
  )
  // 如果是最后一页就不需要执行
  if(data.qrList.len() != index) {
    pagebreak()
  }
}
