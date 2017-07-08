package com.ukelink.test;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.CharsetUtil;

import java.net.InetSocketAddress;

public class EchoServer {

  private final int port;

    public EchoServer(int port) {
        this.port = port;
    }

	public void start() throws Exception {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(group).channel(NioServerSocketChannel.class)
					.localAddress(new InetSocketAddress(port))
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						public void initChannel(SocketChannel ch)
								throws Exception {
							System.out.println("New client connected: "+ ch.localAddress());

							ch.pipeline().addLast(new EchoServerHandler());
						}
					});
			ChannelFuture f = b.bind().sync();
			f.channel().closeFuture().sync();
		} finally {
			group.shutdownGracefully().sync();
		}
	}
	
	public static void main(String[] args) {
		try {
			new EchoServer(12345).start();
			System.out.println("server start finished!");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	class EchoServerHandler extends ChannelInboundHandlerAdapter {

		@Override
		public void channelRegistered(ChannelHandlerContext ctx)
				throws Exception {
			super.channelRegistered(ctx);
			System.out.println("channelRegistered()");
		}

		@Override
		public void channelUnregistered(ChannelHandlerContext ctx)
				throws Exception {
			super.channelUnregistered(ctx);
			System.out.println("channelUnregistered()");
		}

		@Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception {
			super.channelActive(ctx);
			System.out.println("channelActive()");
		}

		@Override
		public void channelInactive(ChannelHandlerContext ctx) throws Exception {
			super.channelInactive(ctx);
			System.out.println("channelInactive()");
		}

		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg)
				throws Exception {
			ByteBuf in = (ByteBuf)msg;
			System.out.println("Server received: " + in.toString(CharsetUtil.UTF_8));
			ctx.writeAndFlush(in);
//			ctx.channel().writeAndFlush(in);
			
		}

		@Override
		public void channelReadComplete(ChannelHandlerContext ctx)
				throws Exception {
			super.channelReadComplete(ctx);
			System.out.println("channelReadComplete()");
		}

		@Override
		public void userEventTriggered(ChannelHandlerContext ctx, Object evt)
				throws Exception {
			super.userEventTriggered(ctx, evt);
			System.out.println("channelWritabilityChanged()");
		}

		@Override
		public void channelWritabilityChanged(ChannelHandlerContext ctx)
				throws Exception {
			System.out.println("channelWritabilityChanged()");
		}

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
				throws Exception {
			super.exceptionCaught(ctx, cause);
			cause.printStackTrace();
			System.out.println("exceptionCaught()" + cause);
//			ctx.close();
		}

		@Override
		public boolean isSharable() {
			System.out.println(super.isSharable());
			return super.isSharable();
		}

		@Override
		public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
			System.out.println("handlerAdded()");
		}

		@Override
		public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
			System.out.println("handlerRemoved()");
		}
		
	}
}


