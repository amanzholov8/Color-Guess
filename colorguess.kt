import org.otfried.cs109ui.*
import org.otfried.cs109ui.ImageCanvas
import org.otfried.cs109.Color
import org.otfried.cs109.DrawStyle

import java.awt.image.BufferedImage

import org.otfried.cs109.readString

val random = java.util.Random()

fun hsvtorgb(h: Int, s: Int, v: Int): Triple<Int, Int, Int> {
  if (s == 0) {
    // no color, just grey
    return Triple(v, v, v)
  } else {
    val sector = h / 60
    val f = (h % 60) 
    val p = v * ( 255 - s ) / 255
    val q = v * ( 15300 - s * f ) / 15300
    val t = v * ( 15300 - s * ( 60 - f )) / 15300
    return when(sector) {
      0 -> Triple(v, t, p)
      1 -> Triple(q, v, p)
      2 -> Triple(p, v, t)
      3 -> Triple(p, q, v)
      4 -> Triple(t, p, v)
      else -> Triple(v, p, q)
    }
  }
}

fun randomHSV(): Triple<Int, Int, Int> {
  return Triple(random.nextInt(360),
  	        128 + random.nextInt(128),
		128 + random.nextInt(128))
}

fun field(image : BufferedImage, delta: Int): String {
  var out = ""
  var (h, s, v) = randomHSV()
  val (r, g, b) = hsvtorgb(h, s, v)
  val d = ImageCanvas(image)
  d.clear(Color.WHITE)
  d.setColor(Color.BLACK)
  d.setFont(22.0, "Batang")
  d.drawText("A", 90.0, 25.0)
  d.drawText("B", 200.0, 25.0)
  d.drawText("C", 310.0, 25.0)
  d.drawText("D", 420.0, 25.0)
  for (i in 1..4)
    d.drawText("$i", 25.0, 100.0 +(108.0 * (i-1)))
  val cube = random.nextInt(16)
  for (i in 0..3)
    for (j in 0..3) {
	  if ((j+1+(4*i)) == cube) {
        val coin = random.nextInt(2)
        if (coin == 1) 
          h+=delta
        else {
          h+=(360-delta)
	      h = h%360
        }	
        val (r1, g1, b1) = hsvtorgb(h, s, v)		
		d.setColor(Color(r1, g1, b1)) 
		out += "${i+1}"
		when (j) {0 -> out+="a"; 1 -> out+="b"; 2 -> out+="c"; 3 -> out+="d"}
	  }
	  else d.setColor(Color(r, g, b))
      d.drawRectangle(50.0+108.0*j, 50.0+108.0*i, 100.0, 100.0, DrawStyle.FILL)	
	}
  d.done()
  return out
}

fun main(args: Array<String>) {
  setTitle("How good is your vision?")
  var delta = 0
  if (args.size==1) 
    delta += args[0].toInt()
  else 
    delta += 20
  var cor = 0
  var all = 0
  while (true) {
    val image = BufferedImage(500, 500, BufferedImage.TYPE_INT_RGB)
    val out = field(image, delta)
    show(image)
    val ans = readString("Which square has a different color? (x to exit) ")
    if (ans == "x")
	  break
    else if (ans == out) {
	  cor += 1
	  all += 1
	  println ("That is correct")
      println ("You answered $cor of $all tests correctly")
    }
    else {
	  all += 1
      println ("That is not correct. Square $out has a different color.")
	  val next = readString("Press Enter for the next question> ")
	  println ("You answered $cor of $all tests correctly")
	  if (next == "") 
	    continue
    }
  }
  close()
}
