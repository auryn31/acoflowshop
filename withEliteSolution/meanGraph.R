antFactor01 <- read.csv(file="config_0_mean.csv", header=FALSE, sep=",")
antFactor02 <- read.csv(file="config_1_mean.csv", header=FALSE, sep=",")
antFactor03 <- read.csv(file="config_2_mean.csv", header=FALSE, sep=",")
antFactor04 <- read.csv(file="config_3_mean.csv", header=FALSE, sep=",")
antFactor05 <- read.csv(file="config_4_mean.csv", header=FALSE, sep=",")
antFactor06 <- read.csv(file="config_5_mean.csv", header=FALSE, sep=",")
antFactor07 <- read.csv(file="config_6_mean.csv", header=FALSE, sep=",")
antFactor08 <- read.csv(file="config_7_mean.csv", header=FALSE, sep=",")
antFactor09 <- read.csv(file="config_8_mean.csv", header=FALSE, sep=",")
antFactor10 <- read.csv(file="config_9_mean.csv", header=FALSE, sep=",")

iteration <- c(antFactor01[1]$V1)

jpeg('result_time_mean_iteration.jpg')
yrange <- range(antFactor01[2]$V2, 
    antFactor02[2]$V2, 
    antFactor03[2]$V2, 
    antFactor04[2]$V2, 
    antFactor05[2]$V2,  
    antFactor06[2]$V2,  
    antFactor07[2]$V2, 
    antFactor08[2]$V2,
    antFactor09[2]$V2,
    antFactor10[2]$V2)

xrange <- range(antFactor01[3]$V3, 
    antFactor02[3]$V3, 
    antFactor03[3]$V3, 
    antFactor04[3]$V3, 
    antFactor05[3]$V3,  
    antFactor06[3]$V3,  
    antFactor07[3]$V3, 
    antFactor08[3]$V3,
    antFactor09[3]$V3,
    antFactor10[3]$V3)

plot(iteration, antFactor01[2]$V2, type="l", ylim=yrange, col="blue", ann=FALSE)
lines(iteration, antFactor02[2]$V2, type="l", col="green")
lines(iteration, antFactor03[2]$V2, type="l", col="chocolate1")
lines(iteration, antFactor04[2]$V2, type="l", col="burlywood3")
lines(iteration, antFactor05[2]$V2, type="l", col="aquamarine4")
lines(iteration, antFactor06[2]$V2, type="l", col="lightblue2")
lines(iteration, antFactor07[2]$V2, type="l", col="magenta1")
lines(iteration, antFactor08[2]$V2, type="l", col="olivedrab1")
lines(iteration, antFactor09[2]$V2, type="l", col="purple2")
lines(iteration, antFactor10[2]$V2, type="l", col="yellow2")

title(main='Anteil der NEH Lösung durch Masterlösung 50 Jobs', font.main=4)
title(xlab='Iteration')
title(ylab='durchschnittliche Fertigstellungszeit')
legend("topright", legend=c("0.0", "0.1", "0.3", "0.4", "0.5", "0.6", "0.7", "0.8", "0.9", "1.0"), 
    col=c("blue", "green", "chocolate1", "burlywood3", "aquamarine4", "lightblue2", "magenta1", "olivedrab1", "purple2", "yellow2"),
    lty=1:1, cex=0.8)

jpeg('result_time_mean_calculationtime.jpg')
plot(antFactor01[3]$V3, antFactor01[2]$V2, type="l", ylim=yrange, xlim=xrange, col="blue", ann=FALSE)
lines(antFactor02[3]$V3, antFactor02[2]$V2, type="l", col="green")
lines(antFactor03[3]$V3, antFactor03[2]$V2, type="l", col="chocolate1")
lines(antFactor04[3]$V3, antFactor04[2]$V2, type="l", col="burlywood3")
lines(antFactor05[3]$V3, antFactor05[2]$V2, type="l", col="aquamarine4")
lines(antFactor06[3]$V3, antFactor06[2]$V2, type="l", col="lightblue2")
lines(antFactor07[3]$V3, antFactor07[2]$V2, type="l", col="magenta1")
lines(antFactor08[3]$V3, antFactor08[2]$V2, type="l", col="olivedrab1")
lines(antFactor09[3]$V3, antFactor09[2]$V2, type="l", col="purple2")
lines(antFactor10[3]$V3, antFactor10[2]$V2, type="l", col="yellow2")

box()

title(main='Anteil der NEH Lösung durch Masterlösung 50 Jobs', font.main=4)
title(xlab='Laufzeit in s')
title(ylab='durchschnittliche Fertigstellungszeit')
legend("topright", legend=c("0.0", "0.1", "0.3", "0.4", "0.5", "0.6", "0.7", "0.8", "0.9", "1.0"), 
    col=c("blue", "green", "chocolate1", "burlywood3", "aquamarine4", "lightblue2", "magenta1", "olivedrab1", "purple2", "yellow2"),
    lty=1:1, cex=0.8)