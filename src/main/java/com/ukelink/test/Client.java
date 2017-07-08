package com.ukelink.test;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;

public class Client {
	//host
    private final String host;
    private final int port;
    /**
     * test comment
     * @param host
     * @param port
     */
    public Client(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void start() throws Exception {
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            Bootstrap b = new Bootstrap();
            b.group(group).channel(NioSocketChannel.class)
                          .remoteAddress(new InetSocketAddress(host, port))
                          .handler(new ChannelInitializer<SocketChannel>() {

                              @Override
                              public void initChannel(SocketChannel ch) throws Exception {
                                  ch.pipeline().addLast(new EchoClientHandler());
                              }
                          });

            ChannelFuture f = b.connect().sync();

            f.channel().closeFuture().sync();
        } finally {
            group.shutdownGracefully().sync();
        }
    }


    public static void main (String [] args) throws Exception {
        new Client("127.0.0.1", 12345).start();//test comment
    }

    
    class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

		@Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception {
			System.out.println("connected!");
			ctx.writeAndFlush(Unpooled.copiedBuffer("Hello World!", CharsetUtil.UTF_8));
		}

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
				throws Exception {
			cause.printStackTrace();
			ctx.close();
		}

		@Override
		protected void channelRead0(ChannelHandlerContext ctx, ByteBuf msg)
				throws Exception {
			System.out.println("Client received message :" + msg.toString(CharsetUtil.UTF_8));
		}
    	
    }
}
