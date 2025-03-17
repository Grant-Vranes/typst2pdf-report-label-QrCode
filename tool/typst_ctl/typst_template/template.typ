// #import "@preview/tablex:0.0.6": tablex, gridx, rowspanx, colspanx, hlinex, vlinex
#import "./tablex/tablex.typ": tablex, gridx, rowspanx, colspanx, hlinex, vlinex

#let data = json.decode("${templateJson}")

// 表头
#set page(
  paper: "a4",
  margin: (top: 50pt, bottom: 10pt),
  header: locate(loc => {[
    #let page-counter = counter(page)
    #let current = page-counter.at(loc)
    #let page = current.first() + 1
    #let page-number = [B] + str(page)
    #set align(start + top)

    #place(start + top)[
      #block(height: 10pt, width: 100%, spacing: 0pt)[]
      #block(height: 30pt, width: 100%, spacing: 0pt, fill: rgb(230, 230, 230), stroke: 0.5pt + black)[
        #set text(font: "Sans CHS", size: 10pt)
        #set par(leading: 2pt)
        #tablex(
          columns: (132pt, 132pt, 100% - 264pt),
          rows: 30pt,
          auto-lines: false,
          stroke: 0.5pt + black,
          align: center + horizon,
          inset: 0pt,
          (), vlinex(), vlinex(), vlinex(),
          [
            #box(width: 100%)[#image("logo.svg", width: 10%, height: auto)]
          ],
          [
            #box(width: 100%)[#data.dept]
          ],
          [
            #box(width: 100%)[#data.titleName]
          ]
        )
      ]
    ]
  ]}),
  // footer-descent: 0pt,
  // footer: []
)

#set text(font: ("Sans CHS"), size: 10pt)

#let record-column-width = (100%) / 3

// record信息
#block()[
  #let recordContent = ()
  #for record in data.recordData {
    recordContent.push([
      #record.recordName
    ])
    recordContent.push([
      #record.recordDescription
    ])
    recordContent.push([
      #record.recordTime
    ])
  }
  #gridx(
      columns: (record-column-width, record-column-width, record-column-width),
      inset: (x: 0pt, y: 0pt),
      width: 100%,
      map-cells: cell => {
        let fill = none

        cell.align = start + top
        cell.content = [
          #block(spacing: 10pt, inset: 10pt)[#cell.content]
        ]
        cell.content = style(styles => {
          let size = measure(cell.content, styles)
          if size.width <= 25pt {
            return block(height: 25pt, width: 100%, spacing: 0pt)[#cell.content]
          }
          return cell.content
        })
        // 设置背景颜色
        if ((cell.x == 0 or cell.x == 1 or cell.x == 2 or cell.x == 3 or cell.x == 4) and cell.y == 0) {
          fill = rgb(230, 230, 230)
        }
        cell.content = [
          #place(start + top, clearance: 0pt, dx: 0pt, dy: 0pt)[
            #rect(height: 100%, width: 100%, stroke: 0.5pt + black, fill: fill)
          ]
          #cell.content
        ]
        return cell
      },
      [
        #set align(center + horizon)
        #set text(font: "Sans CHS", size: 10pt, weight: "bold")
        记录名称
      ],
      [
        #set align(center + horizon)
        #set text(font: "Sans CHS", size: 10pt, weight: "bold")
        记录描述
      ],
      [
        #set align(center + horizon)
        #set text(font: "Sans CHS", size: 10pt, weight: "bold")
        记录时间
      ],
      ..recordContent
    )
]

#block(height: 15pt, width: 100%, spacing: 0pt)[]
